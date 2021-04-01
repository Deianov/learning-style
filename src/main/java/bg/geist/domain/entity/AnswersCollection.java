package bg.geist.domain.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "answers_collections")
public class AnswersCollection extends BaseEntity{

    @Basic
    private String name;

    @Basic
    private int value;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "collection")
    private Collection<Answer> answers;

    public AnswersCollection() {}
    public AnswersCollection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public AnswersCollection setName(String name) {
        this.name = name;
        return this;
    }

    public int getValue() {
        return value;
    }

    public AnswersCollection setValue(int value) {
        this.value = value;
        return this;
    }

    public Collection<Answer> getAnswers() {
        return answers;
    }

    public AnswersCollection setAnswers(Collection<Answer> answers) {
        this.answers = answers;
        return this;
    }
}
