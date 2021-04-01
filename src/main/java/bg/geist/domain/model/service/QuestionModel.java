package bg.geist.domain.model.service;

import java.util.Collection;

public class QuestionModel {
    private String text;
    private int value;
    private int correct;
    private String type;
    private Collection<AnswerModel> answers;


    public String getText() {
        return text;
    }

    public QuestionModel setText(String text) {
        this.text = text;
        return this;
    }

    public int getValue() {
        return value;
    }

    public QuestionModel setValue(int value) {
        this.value = value;
        return this;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Collection<AnswerModel> getAnswers() {
        return answers;
    }

    public QuestionModel setAnswers(Collection<AnswerModel> answers) {
        this.answers = answers;
        return this;
    }
}
