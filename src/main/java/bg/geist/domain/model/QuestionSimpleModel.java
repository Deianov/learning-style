package bg.geist.domain.model;

import java.util.Collection;

public class QuestionSimpleModel {
    private String text;
    private int value;
    private Collection<AnswerModel> answers;


    public QuestionSimpleModel() {}

    public String getText() {
        return text;
    }

    public QuestionSimpleModel setText(String text) {
        this.text = text;
        return this;
    }

    public int getValue() {
        return value;
    }

    public QuestionSimpleModel setValue(int value) {
        this.value = value;
        return this;
    }

    public Collection<AnswerModel> getAnswers() {
        return answers;
    }

    public QuestionSimpleModel setAnswers(Collection<AnswerModel> answers) {
        this.answers = answers;
        return this;
    }
}