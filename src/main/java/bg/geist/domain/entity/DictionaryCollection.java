package bg.geist.domain.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;


@Entity
@Table(name = "dictionary_collections")
public class DictionaryCollection extends BaseEntity{

    @Basic
    private String name;

    @Basic
    private int value;

    @OneToMany(fetch =FetchType.EAGER, mappedBy = "collection")
    private Collection<Dictionary> dictionaries = new ArrayList<>();


    public DictionaryCollection() { }
    public DictionaryCollection(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public DictionaryCollection setName(String name) {
        this.name = name;
        return this;
    }

    public int getValue() {
        return value;
    }

    public DictionaryCollection setValue(int value) {
        this.value = value;
        return this;
    }

    public Collection<Dictionary> getDictionaries() {
        return dictionaries;
    }

    public DictionaryCollection setDictionaries(Collection<Dictionary> dictionaries) {
        this.dictionaries = dictionaries;
        return this;
    }
}