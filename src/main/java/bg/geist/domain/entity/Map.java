package bg.geist.domain.entity;

import bg.geist.domain.entity.enums.ExerciseType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "maps")
public class Map extends ExercisePlay {
    private static final ExerciseType EXERCISE_TYPE = ExerciseType.MAPS;

    @OneToOne
    private Exercise exercise;

    public Map() { super(EXERCISE_TYPE); }
    public Map(Exercise exercise) {
        super();
        this.exercise = exercise;
    }


    public Exercise getExercise() {
        return exercise;
    }
    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Map map = (Map) o;
        return exercise.equals(map.exercise);
    }
    @Override
    public int hashCode() {
        return Objects.hash(exercise);
    }
}
