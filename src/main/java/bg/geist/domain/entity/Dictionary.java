package bg.geist.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;
import java.util.stream.Stream;

@Entity
@Table(name = "dictionary")
public class Dictionary{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column
    private int value = 0;

    @Column(nullable = false)
    private String att1;

    @Column(nullable = false)
    private String att2;

    @Column
    private String att3;

    @JsonIgnore
    @ManyToOne
    private DictionaryCollection collection;


    public Dictionary() {}
    public Dictionary(int value , String[] arr) {
        this.update(arr);
        this.value = value;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getAtt1() {
        return att1;
    }

    public void setAtt1(String att1) {
        this.att1 = att1;
    }

    public String getAtt2() {
        return att2;
    }

    public void setAtt2(String att2) {
        this.att2 = att2;
    }

    public String getAtt3() {
        return att3;
    }

    public void setAtt3(String att3) {
        this.att3 = att3;
    }

    public DictionaryCollection getCollection() {
        return collection;
    }

    public void setCollection(DictionaryCollection collection) {
        this.collection = collection;
    }

    // custom methods

    public String[] toArray() {
        return Stream.of(att1, att2, att3).filter(Objects::nonNull).toArray(String[]::new);
    }

    public void update(String[] arr) {
        if (arr == null) {
            return;
        }
        // move index if arr has value
        int m;
        if (att1 == null) {
            // constructor
            m = 0;
        } else {
            // update (first index is row number)
            m = (arr.length > ((att3 == null) ? 2 : 3)) ? 1 : 0;
        }
        if ((arr.length + m) < 2) {
            return;
        }
        this.att1 = arr[m];
        this.att2 = arr[1 + m];
        this.att3 = arr.length > (2 + m) ? arr[2 + m] : null;
    }

    public boolean equalsTo(String[] arr) {
        return compareTo(arr, true) == 3;
    }

    // arr = ["value", att1, att2, att3] || [att1, att2, att3]
    public int compareTo(String[] arr, boolean compareValues) {
        if (arr == null || arr.length < 3 || att1 == null || att2 == null || value == 0) {
            return 0;
        }
        int i = 0;
        boolean hasValue = arr.length > ((att3 == null) ? 2 : 3);
        // compare values
        if (!compareValues || !hasValue || String.valueOf(value).equals(arr[0])) {
            i += 1;
        }
        int m = hasValue ? 1 : 0;
        // compare attributes
        if (att1.equals(arr[m]) && (att2.equals(arr[1 + m]) && (att3 == null || att3.equals(arr[2 + m])))) {
            i += 2;
        }
        return i;
    }

    // todo: change with business id

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dictionary that = (Dictionary) o;
        return value == that.value && att1.equals(that.att1) && att2.equals(that.att2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, att1, att2);
    }
}