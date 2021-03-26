package bg.geist.domain.entity;

import bg.geist.domain.enums.ExerciseType;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "cards")
public class Cards extends Exercise{

    @OneToOne
    private Options labels;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dictionary_collection_id")
    private DictionaryCollection dictionaryCollection;


    public Cards() {
        super(ExerciseType.CARDS);
    }
    public Cards(String name, String description, int value) {
        super(ExerciseType.CARDS, name, description, value);
    }


    public Options getLabels() {
        return labels;
    }

    public Cards setLabels(Options labels) {
        this.labels = labels;
        return this;
    }

    public DictionaryCollection getDictionaryCollection() {
        return dictionaryCollection;
    }

    public Cards setDictionaryCollection(DictionaryCollection dictionaryCollection) {
        this.dictionaryCollection = dictionaryCollection;
        return this;
    }
}