package bg.geist.web.api.quiz.models;

import java.util.Collection;

public class QuizValidationResponseModel {
    private Collection<Integer> correct;
    private int right;
    private int wrong;

    public QuizValidationResponseModel() { }
    public QuizValidationResponseModel(Collection<Integer> correct) {
        this.correct = correct;
    }

    public Collection<Integer> getCorrect() {
        return correct;
    }

    public QuizValidationResponseModel setCorrect(Collection<Integer> correct) {
        this.correct = correct;
        return this;
    }

    public int getRight() {
        return right;
    }

    public QuizValidationResponseModel setRight(int right) {
        this.right = right;
        return this;
    }

    public int getWrong() {
        return wrong;
    }

    public QuizValidationResponseModel setWrong(int wrong) {
        this.wrong = wrong;
        return this;
    }
}