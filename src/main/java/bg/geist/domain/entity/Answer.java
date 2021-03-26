package bg.geist.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "answers")
public class Answer extends BaseEntity{

    @Column(nullable = false)
    private String text;

    @Basic
    private int value;

    @ManyToOne
    private AnswersCollection collection;

    public Answer() {}
    public Answer(String text, int value, AnswersCollection collection) {
        this.text = text;
        this.value = value;
        this.collection = collection;
    }

    public String getText() {
        return text;
    }

    public Answer setText(String text) {
        this.text = text;
        return this;
    }

    public int getValue() {
        return value;
    }

    public Answer setValue(int value) {
        this.value = value;
        return this;
    }

    public AnswersCollection getCollection() {
        return collection;
    }

    public Answer setCollection(AnswersCollection collection) {
        this.collection = collection;
        return this;
    }
}
