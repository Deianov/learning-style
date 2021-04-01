package bg.geist.web.api.quiz.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;


public class QuizCertificationRequestModel {
    @NotNull
    private Long quizId;

    @NotEmpty
    @NotNull
    private Collection<Integer> answers;

    public QuizCertificationRequestModel() { }

    public Long getQuizId() {
        return quizId;
    }

    public QuizCertificationRequestModel setQuizId(Long quizId) {
        this.quizId = quizId;
        return this;
    }

    public Collection<Integer> getAnswers() {
        return answers;
    }

    public QuizCertificationRequestModel setAnswers(Collection<Integer> answers) {
        this.answers = answers;
        return this;
    }
}