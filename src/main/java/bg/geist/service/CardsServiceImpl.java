package bg.geist.service;

import bg.geist.constant.Constants;
import bg.geist.domain.entity.Cards;
import bg.geist.domain.entity.Category;
import bg.geist.domain.entity.Dictionary;
import bg.geist.domain.entity.Options;
import bg.geist.exception.EntityName;
import bg.geist.domain.model.service.CardsModel;
import bg.geist.exception.ObjectNotFoundException;
import bg.geist.repository.CardsRepository;
import bg.geist.repository.CategoryRepository;
import bg.geist.web.api.model.ExerciseIndexModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CardsServiceImpl implements CardsService {

    private final ModelMapper mapper;
    private final CardsRepository repository;
    private final CategoryRepository categoryRepository;

    public CardsServiceImpl(ModelMapper mapper, CardsRepository repository, CategoryRepository categoryRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Collection<ExerciseIndexModel> getIndex() {
        Collection<ExerciseIndexModel> result = new ArrayList<>();
        Collection<Category> categories = categoryRepository.
                findAllByParentId(Constants.CATEGORY_ID_CARDS);

        for (Category category : categories) {
            ExerciseIndexModel exerciseCategory = new ExerciseIndexModel(category.getName());
            repository
                    .findAllByCategoryId(category.getId())
                    .forEach(cards -> exerciseCategory.addLink(cards.getId(), cards.getName()));
            // skip empty categories
            if (!exerciseCategory.getLinks().isEmpty()) {
                result.add(exerciseCategory);
            }
        }
        return result;
    }

    @Override
    public CardsModel getById(Long id) {
        Cards cards = repository.fetchById(id)
                .orElseThrow(() -> new ObjectNotFoundException(EntityName.CARDS, id));

        CardsModel cardsModel = mapper.map(cards, CardsModel.class);
        categoryRepository.findById(cards.getCategoryId())
                .ifPresent(category -> cardsModel.setCategory(category.getName()));
        Options labels = cards.getLabels();
        if (labels != null) {
            cardsModel.setLabels(labels.getKeys());
        }
        Options options = cards.getOptions();
        if (options != null) {
            cardsModel.setOptions(options.toMap());
            cardsModel.getOptions().remove("adapter");
        }
        cardsModel.setDictionaries(cards.getDictionaryCollection().getDictionaries()
                .stream()
                .map(Dictionary::toArray)
                .toArray(String[][]::new)
        );
        return cardsModel;
    }
}