package bg.geist.repository;

import bg.geist.domain.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    boolean existsByName(String name);
    Optional<Exercise> findByName(String name);
}
