package bg.geist.service;

import bg.geist.domain.entity.Category;
import bg.geist.domain.entity.enums.ExerciseType;
import bg.geist.repository.CategoryRepository;
import bg.geist.web.api.model.ExerciseIndexModel;
import bg.geist.web.api.quizzes.QuizController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final String NOT_FOUND_PARENT = "Not found parent directory: %s";
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }


    @Override
    public Collection<Category> getCategories(ExerciseType exerciseType) {
        return getParent(exerciseType).map(parent -> getChildren(parent.getId())).orElse(null);
    }

    @Override
    public Collection<Category> getChildren(Long parentId) {
        return repository.findAllByParentId(parentId);
    }

    @Override
    public Category getByName(String name, ExerciseType exerciseType){
        return getParent(exerciseType).map(parent -> repository.findByNameAndParentId(name, parent.getId())).orElse(null);
    }

    private Optional<Category> getParent(ExerciseType exerciseType){
        String name = exerciseType == null ? null : exerciseType.toString();
        Optional<Category> parent = repository.findByNameAndParentIdIsNull(name);
        if (parent.isEmpty()) {
            logger.error(String.format(NOT_FOUND_PARENT, name));
        }
        return parent;
    }
}