package bg.geist.web.api.quiz.models;


public class AnswerResponseModel {
    private String text;
    private int value;


    public String getText() {
        return text;
    }

    public AnswerResponseModel setText(String text) {
        this.text = text;
        return this;
    }

    public int getValue() {
        return value;
    }

    public AnswerResponseModel setValue(int value) {
        this.value = value;
        return this;
    }
}
