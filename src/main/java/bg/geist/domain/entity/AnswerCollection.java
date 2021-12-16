package bg.geist.domain.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "answers_collections")
public class AnswerCollection extends BaseEntity{

    @Basic
    private String name;

    @Basic
    private int value;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "collection")
    private Collection<Answer> answers;

    public AnswerCollection() {}
    public AnswerCollection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public AnswerCollection setName(String name) {
        this.name = name;
        return this;
    }

    public int getValue() {
        return value;
    }

    public AnswerCollection setValue(int value) {
        this.value = value;
        return this;
    }

    public Collection<Answer> getAnswers() {
        return answers;
    }

    public AnswerCollection setAnswers(Collection<Answer> answers) {
        this.answers = answers;
        return this;
    }
}
