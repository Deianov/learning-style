package bg.geist.repository;

import bg.geist.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    Optional<Category> findByNameAndParentIdIsNull(String name);
    Category findByNameAndParentId(String name, Long parentId);
    Collection<Category> findAllByParentId(Long parentId);
}