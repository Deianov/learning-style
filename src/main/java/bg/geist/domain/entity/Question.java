package bg.geist.domain.entity;

import bg.geist.domain.entity.enums.Lang;
import bg.geist.domain.entity.enums.Level;

import javax.persistence.*;

@Entity
@Table(name = "questions")
public class Question extends BaseEntity{

    public enum Type {
        BOOLEAN, SINGLE, MULTIPLE;
    }

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Basic
    private int value;

    @Enumerated(EnumType.STRING)
    @Basic
    private Type type;

    @Basic
    private int correct;

    @ManyToOne
    @JoinColumn(name = "answers_collection_id")
    private AnswerCollection answerCollection;

    public Question() {}
    public Question(String text) {
        this.text = text;
    }
    public Question(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public Question setText(String text) {
        this.text = text;
        return this;
    }

    public int getValue() {
        return value;
    }

    public Question setValue(int value) {
        this.value = value;
        return this;
    }

    public Type getType() {
        return type;
    }

    public Question setType(Type type) {
        this.type = type;
        return this;
    }

    public int getCorrect() {
        return correct;
    }

    public Question setCorrect(int correct) {
        this.correct = correct;
        return this;
    }

    public AnswerCollection getAnswersCollection() {
        return answerCollection;
    }

    public Question setAnswersCollection(AnswerCollection answerCollection) {
        this.answerCollection = answerCollection;
        return this;
    }
}