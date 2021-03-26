package bg.geist.domain.entity;

import bg.geist.domain.enums.ExerciseType;

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
        super(ExerciseType.QUIZ);
    }
    public Quiz(String name, String description, int value) {
        super(ExerciseType.QUIZ, name, description, value);
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