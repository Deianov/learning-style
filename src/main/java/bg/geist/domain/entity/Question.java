package bg.geist.domain.entity;

import bg.geist.domain.enums.Lang;
import bg.geist.domain.enums.Level;

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
    private Level level;

    @Enumerated(EnumType.STRING)
    @Basic
    private Lang lang;

    @Enumerated(EnumType.STRING)
    @Basic
    private Type type;

    @Basic
    private int correct;

    @ManyToOne
    @JoinColumn(name = "answers_collection_id")
    private AnswersCollection answersCollection;

    public Question() {}
    public Question(String text) {
        this.text = text;
    }
    public Question(String text, int value, Level level, Lang lang) {
        this.text = text;
        this.value = value;
        this.level = level;
        this.lang = lang;
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

    public Level getLevel() {
        return level;
    }

    public Question setLevel(Level level) {
        this.level = level;
        return this;
    }

    public Lang getLang() {
        return lang;
    }

    public Question setLang(Lang lang) {
        this.lang = lang;
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

    public AnswersCollection getAnswersCollection() {
        return answersCollection;
    }

    public Question setAnswersCollection(AnswersCollection answersCollection) {
        this.answersCollection = answersCollection;
        return this;
    }
}