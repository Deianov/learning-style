package bg.geist.domain.model.service;


public class AnswerModel {
    private String text;
    private int value;


    public String getText() {
        return text;
    }

    public AnswerModel setText(String text) {
        this.text = text;
        return this;
    }

    public int getValue() {
        return value;
    }

    public AnswerModel setValue(int value) {
        this.value = value;
        return this;
    }
}
