package bg.geist.domain.entity;


import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "dictionary_collections")
public class DictionaryCollection extends BaseEntity{

    @Basic
    private String name;

    @Basic
    private int value;

    @OneToMany(fetch = FetchType.EAGER , cascade = CascadeType.PERSIST, mappedBy = "collection")
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

    public void add(Dictionary dictionary) {
        dictionary.setCollection(this);
        dictionaries.add(dictionary);
    }

    public void remove(Dictionary dictionary) {
        dictionary.setCollection(null);
        dictionaries.remove(dictionary);
    }

    public boolean exists (String[] dictionary) {
        return dictionaries.stream().anyMatch(d -> d.compareTo(dictionary, false) > 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryCollection that = (DictionaryCollection) o;
        return value == that.value && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}