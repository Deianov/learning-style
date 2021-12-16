package bg.geist.service;

import bg.geist.domain.entity.*;
import bg.geist.domain.entity.enums.ExerciseType;
import bg.geist.domain.model.service.MapModel;
import bg.geist.exception.EntityName;
import bg.geist.exception.ObjectNotFoundException;
import bg.geist.repository.MapRepository;
import bg.geist.web.api.model.ExerciseIndexModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class MapServiceImpl implements MapService {
    private static final ExerciseType EXERCISE_TYPE = ExerciseType.MAPS;
    private static final EntityName ENTITY_NAME = EntityName.MAP;

    private final MapRepository repository;
    private final CategoryService categoryService;
    private final ExerciseService exerciseService;


    @Autowired
    public MapServiceImpl(MapRepository repository, CategoryService categoryService, ExerciseService exerciseService) {
        this.repository = repository;
        this.categoryService = categoryService;
        this.exerciseService = exerciseService;
    }

    @Override
    public Collection<ExerciseIndexModel> getIndex() {
        Collection<ExerciseIndexModel> result = new ArrayList<>();

        for (Category category : categoryService.getCategories(EXERCISE_TYPE)) {
            ExerciseIndexModel exerciseCategory = new ExerciseIndexModel(category.getName());
            repository
                .findAllByCategory(category)
                .forEach(type -> exerciseCategory.addLink(type.getId(), type.getExercise().getName()));
            // skip empty categories
            if (!exerciseCategory.getLinks().isEmpty()) {
                result.add(exerciseCategory);
            }
        }
        return result;
    }

    @Override
    public MapModel getById(Long id) {
        Map map = getBy(id);
        MapModel mapModel = new MapModel();
        mapModel.setExercise(exerciseService.map(map.getExercise(), map));
        mapModel.setProps(Prop.toMap(map.getProps()));
        return mapModel;
    }

    private Map getBy(Long id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(ENTITY_NAME, id));
    }
}