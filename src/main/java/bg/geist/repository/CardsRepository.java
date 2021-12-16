package bg.geist.repository;

import bg.geist.domain.entity.Cards;
import bg.geist.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CardsRepository extends JpaRepository<Cards, Long> {
    Collection<Cards> findAllByCategory(Category category);

    // force eager fetch by custom HQL query
    @Query("select e from Cards e left join fetch e.dictionaryCollection where e.id = ?1")
    Optional<Cards> fetchById(Long id);
}