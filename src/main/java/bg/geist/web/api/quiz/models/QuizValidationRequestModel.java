package bg.geist.web.api.quiz.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;


public class QuizValidationRequestModel {
    @NotNull
    private Long quizId;

    @NotEmpty
    @NotNull
    private Collection<Integer> answers;

    public QuizValidationRequestModel() { }

    public Long getQuizId() {
        return quizId;
    }

    public QuizValidationRequestModel setQuizId(Long quizId) {
        this.quizId = quizId;
        return this;
    }

    public Collection<Integer> getAnswers() {
        return answers;
    }

    public QuizValidationRequestModel setAnswers(Collection<Integer> answers) {
        this.answers = answers;
        return this;
    }
}