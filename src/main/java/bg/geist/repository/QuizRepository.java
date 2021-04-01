package bg.geist.repository;

import bg.geist.domain.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    Collection<Quiz> findAllByCategoryId(long categoryId);

    // force eager fetch by custom HQL query
    @Query("select e from Quiz e left join fetch e.questions b where e.id = ?1")
    Optional<Quiz> fetchById(long id);

    @Query(value = "select q.correct from quizzes e "+
            "left join quizzes_questions qq on e.id = qq.quiz_id "+
            "left join questions q on q.id = qq.questions_id "+
            "where e.id = ?1", nativeQuery = true)
    Collection<Integer> getCorrectAnswers(Long id);
}