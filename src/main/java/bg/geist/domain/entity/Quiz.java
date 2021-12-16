package bg.geist.domain.entity;

import bg.geist.domain.entity.enums.ExerciseType;
import bg.geist.domain.entity.enums.Lang;
import bg.geist.domain.entity.enums.Level;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "quizzes")
public class Quiz extends ExercisePlay {
    private static final ExerciseType EXERCISE_TYPE = ExerciseType.QUIZZES;

    @OneToOne
    private Exercise exercise;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Question> questions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Basic
    private Level level;

    @Enumerated(EnumType.STRING)
    @Basic
    private Lang lang;


    public Quiz() { super(EXERCISE_TYPE); }
    public Quiz(Exercise exercise) {
        super();
        this.exercise = exercise;
    }


    public Collection<Question> getQuestions() {
        return questions;
    }
    public Quiz setQuestions(Collection<Question> questions) {
        this.questions = questions;
        return this;
    }
    public Exercise getExercise() {
        return exercise;
    }
    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
    public Level getLevel() {
        return level;
    }
    public void setLevel(Level level) {
        this.level = level;
    }
    public Lang getLang() {
        return lang;
    }
    public void setLang(Lang lang) {
        this.lang = lang;
    }


    public Collection<Integer> getCorrect() {
        return this.questions.stream().map(Question::getCorrect).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quiz quiz = (Quiz) o;
        return exercise.equals(quiz.exercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exercise);
    }
}