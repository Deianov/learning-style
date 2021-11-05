package bg.geist.service;

import bg.geist.constant.Constants;
import bg.geist.domain.entity.Category;
import bg.geist.domain.entity.Map;
import bg.geist.domain.model.service.MapModel;
import bg.geist.exception.EntityName;
import bg.geist.exception.ObjectNotFoundException;
import bg.geist.repository.CategoryRepository;
import bg.geist.repository.MapRepository;
import bg.geist.web.api.exercise.ExerciseIndexModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class MapServiceImpl implements MapService {

    private final MapRepository repository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;


    @Autowired
    public MapServiceImpl(MapRepository repository, CategoryRepository categoryRepository, ModelMapper mapper) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public Collection<ExerciseIndexModel> getIndex() {
        Collection<ExerciseIndexModel> result = new ArrayList<>();
        Collection<Category> categories = categoryRepository.
                findAllByParentId(Constants.CATEGORY_ID_MAPS);

        for (Category category : categories) {
            ExerciseIndexModel exerciseCategory = new ExerciseIndexModel(category.getName());
            repository
                    .findAllByCategoryId(category.getId())
                    .forEach(maps -> exerciseCategory.addLink(maps.getId(), maps.getName()));
            // skip empty categories
            if (!exerciseCategory.getLinks().isEmpty()) {
                result.add(exerciseCategory);
            }
        }
        return result;
    }

    @Override
    public MapModel getById(Long id) {
        Map map = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(EntityName.MAP, id));

        MapModel mapModel = mapper.map(map, MapModel.class);
        categoryRepository.findById(map.getCategoryId())
                .ifPresent(category -> mapModel.setCategory(category.getName()));
        return mapModel;
    }
}