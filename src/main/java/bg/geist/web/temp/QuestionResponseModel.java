package bg.geist.web.api.quiz.models;

import java.util.Collection;

public class QuestionResponseModel {
    private String text;
    private int value;
    private int correct;
    private String type;
    private Collection<AnswerResponseModel> answers;


    public String getText() {
        return text;
    }

    public QuestionResponseModel setText(String text) {
        this.text = text;
        return this;
    }

    public int getValue() {
        return value;
    }

    public QuestionResponseModel setValue(int value) {
        this.value = value;
        return this;
    }

    public int getCorrect() {
        return correct;
    }

    public QuestionResponseModel setCorrect(int correct) {
        this.correct = correct;
        return this;
    }

    public String getType() {
        return type;
    }

    public QuestionResponseModel setType(String type) {
        this.type = type;
        return this;
    }

    public Collection<AnswerResponseModel> getAnswers() {
        return answers;
    }

    public QuestionResponseModel setAnswers(Collection<AnswerResponseModel> answers) {
        this.answers = answers;
        return this;
    }
}
