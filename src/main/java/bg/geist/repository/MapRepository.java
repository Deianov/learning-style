package bg.geist.repository;

import bg.geist.domain.entity.Category;
import bg.geist.domain.entity.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface MapRepository extends JpaRepository<Map, Long> {
    Collection<Map> findAllByCategory(Category category);
    Optional<Map> findById(Long id);
}