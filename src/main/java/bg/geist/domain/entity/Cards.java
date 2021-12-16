package bg.geist.domain.entity;

import bg.geist.domain.entity.enums.ExerciseType;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cards")
public class Cards extends ExercisePlay{
    private static final ExerciseType EXERCISE_TYPE = ExerciseType.CARDS;

    @OneToOne
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dictionary_collection_id")
    private DictionaryCollection dictionaryCollection;


    public Cards() { super(EXERCISE_TYPE); }
    public Cards(Exercise exercise) {
        this();
        this.exercise = exercise;
    }


    public Exercise getExercise() {
        return exercise;
    }
    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
    public DictionaryCollection getDictionaryCollection() {
        return dictionaryCollection;
    }
    public void setDictionaryCollection(DictionaryCollection dictionaryCollection) {
        this.dictionaryCollection = dictionaryCollection;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cards cards = (Cards) o;
        return exercise.equals(cards.exercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exercise);
    }
}