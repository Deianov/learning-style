package bg.geist.service;

import bg.geist.domain.entity.*;
import bg.geist.domain.entity.Dictionary;
import bg.geist.domain.entity.enums.ExerciseType;
import bg.geist.exception.EntityName;
import bg.geist.domain.model.service.CardsModel;
import bg.geist.exception.ObjectNotFoundException;
import bg.geist.repository.CardsRepository;
import bg.geist.repository.DictionaryCollectionRepository;
import bg.geist.repository.DictionaryRepository;
import bg.geist.web.api.model.ExerciseIndexModel;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CardsServiceImpl implements CardsService {
    private static final ExerciseType EXERCISE_TYPE = ExerciseType.CARDS;
    private static final EntityName ENTITY_NAME = EntityName.CARDS;

    private final CardsRepository repository;
    private final CategoryService categoryService;
    private final DictionaryRepository dictionaryRepository;
    private final DictionaryCollectionRepository dictionaryCollectionRepository;
    private final ExerciseService exerciseService;


    public CardsServiceImpl(CardsRepository repository, CategoryService categoryService, DictionaryRepository dictionaryRepository, DictionaryCollectionRepository dictionaryCollectionRepository, ExerciseService exerciseService) {
        this.repository = repository;
        this.categoryService = categoryService;
        this.dictionaryRepository = dictionaryRepository;
        this.dictionaryCollectionRepository = dictionaryCollectionRepository;
        this.exerciseService = exerciseService;
    }

    @Override
    public Collection<ExerciseIndexModel> getIndex() {
        Collection<ExerciseIndexModel> result = new ArrayList<>();

        for (Category category : categoryService.getCategories(EXERCISE_TYPE)) {
            ExerciseIndexModel exerciseCategory = new ExerciseIndexModel(category.getName());
            repository
                .findAllByCategory(category)
                .forEach(cards -> exerciseCategory.addLink(cards.getId(), cards.getExercise().getName()));
            // skip empty categories
            if (!exerciseCategory.getLinks().isEmpty()) {
                result.add(exerciseCategory);
            }
        }
        return result;
    }

    @Override
    public CardsModel getById(Long id) {
        Cards cards = getBy(id);
        CardsModel cardsModel = new CardsModel();
        cardsModel.setExercise(exerciseService.map(cards.getExercise(), cards));
        cardsModel.setProps(Prop.toMap(cards.getProps()));
        cardsModel.getProps().remove("adapter");
        cardsModel.setData(cards.getDictionaryCollection().getDictionaries()
            .stream()
            .map(Dictionary::toArray)
            .toArray(String[][]::new)
        );
        return cardsModel;
    }

    @Override
    public int update(Long id, CardsModel model) {
        Cards cards = getBy(id);

        int i = 0;
        String[][] data = model.getData();
        DictionaryCollection dictionaries = cards.getDictionaryCollection();

        Collection<Dictionary> tmp = new ArrayList<>();
        Collection<Dictionary> created = new ArrayList<>();

        // deleted or edited
        for (Dictionary dictionary : dictionaries.getDictionaries()) {
            /* value - unique row number for dictionaries */
            String[] arr = Arrays.stream(data)
                .filter(row -> row[0].equals(String.valueOf(dictionary.getValue())))
                .findFirst()
                .orElse(null);
            // is deleted
            if (arr == null) {
                tmp.add(dictionary);
                i++;
            };
            // is edited;
            if (arr != null && !dictionary.equalsTo(arr)) {
                dictionary.update(arr);
                dictionaryRepository.save(dictionary);
                i++;
            }
        }
        tmp.forEach(dictionaries::remove);

        // renumbering
        int c = 1;
        for (Dictionary dictionary : dictionaries.getDictionaries()) {
            if (dictionary.getValue() != c) {
                dictionary.setValue(c);
                dictionaryRepository.save(dictionary);
            }
            c++;
        }

        // created
        for (String[] arr : model.getData()) {
            if (!dictionaries.exists(arr)) {
                created.add(new Dictionary(c++, arr));

            }
        }
        for (Dictionary dictionary: created) {
            dictionary.setCollection(dictionaries);
            dictionaries.add(dictionaryRepository.save(dictionary));
            i++;
        }

        // final update
        if (i > 0) {
            dictionaries.setValue(dictionaries.getDictionaries().size());
            dictionaryCollectionRepository.save(dictionaries);
            cards.setUpdated(new Date());
            repository.save(cards);
            dictionaryRepository.deleteInBatch(tmp);
        }

        return i;
    }

    private Cards getBy(Long id) {
        return repository.fetchById(id).orElseThrow(() -> new ObjectNotFoundException(ENTITY_NAME, id));
    }
}