package bg.geist.repository;

import bg.geist.domain.entity.AnswerCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerCollectionRepository extends JpaRepository<AnswerCollection, Long> {
}
