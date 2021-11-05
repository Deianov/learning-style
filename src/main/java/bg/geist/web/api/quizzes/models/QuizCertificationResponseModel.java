package bg.geist.web.api.quizzes.models;

import java.util.Collection;

public class QuizCertificationResponseModel {
    private Collection<Integer> correct;
    private int right;
    private int wrong;

    public QuizCertificationResponseModel() { }
    public QuizCertificationResponseModel(Collection<Integer> correct) {
        this.correct = correct;
    }

    public Collection<Integer> getCorrect() {
        return correct;
    }

    public QuizCertificationResponseModel setCorrect(Collection<Integer> correct) {
        this.correct = correct;
        return this;
    }

    public int getRight() {
        return right;
    }

    public QuizCertificationResponseModel setRight(int right) {
        this.right = right;
        return this;
    }

    public int getWrong() {
        return wrong;
    }

    public QuizCertificationResponseModel setWrong(int wrong) {
        this.wrong = wrong;
        return this;
    }
}