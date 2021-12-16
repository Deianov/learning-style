package bg.geist.service;

import bg.geist.domain.entity.Category;
import bg.geist.domain.entity.enums.ExerciseType;

import java.util.Collection;

public interface CategoryService {
    Collection<Category> getCategories(ExerciseType exerciseType);
    Collection<Category> getChildren(Long parentId);
    Category getByName(String name, ExerciseType exerciseType);
}