package bg.geist.repository;

import bg.geist.domain.entity.AnswersCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswersCollectionRepository extends JpaRepository<AnswersCollection, Long> {
}
