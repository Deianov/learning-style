package bg.geist.domain.entity;

import bg.geist.domain.entity.enums.ExerciseType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(name = "quizzes")
public class Quiz extends Exercise{

    @ManyToMany
    private Collection<Question> questions = new ArrayList<>();


    public Quiz() {
        super(ExerciseType.QUIZZES);
    }
    public Quiz(String name, String description, int value) {
        super(ExerciseType.QUIZZES, name, description, value);
    }

    public Collection<Question> getQuestions() {
        return questions;
    }

    public Quiz setQuestions(Collection<Question> questions) {
        this.questions = questions;
        return this;
    }

    public Collection<Integer> getCorrect() {
        return this.questions.stream().map(Question::getCorrect).collect(Collectors.toCollection(ArrayList::new));
    }
}