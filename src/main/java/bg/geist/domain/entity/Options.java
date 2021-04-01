package bg.geist.domain.entity;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "options")
public class Options extends BaseEntity{
    @Basic
    private String key1;
    @Basic
    private Integer val1;
    @Basic
    private String key2;
    @Basic
    private Integer val2;
    @Basic
    private String key3;
    @Basic
    private Integer val3;


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // test
    private Options next;


    public HashMap<String, Integer> toMap() {
        HashMap<String, Integer> result = new HashMap<>();
        if(key1 != null) { result.put(key1, val1); }
        if(key2 != null) { result.put(key2, val2); }
        if(key3 != null) { result.put(key3, val3); }
        if(next != null) {
            result.putAll(this.next.toMap());
        }
        return result;
    }
    
    public Options fromMap(HashMap<String, Integer> map) {
        map.forEach(this::add);
        return this;
    }

    public Options add(String key) { return this.add(key, null); }
    public Options add(String key, Integer val) {
        if(this.key1 == null) { this.key1 = key; this.val1 = val; }
        else if(this.key2 == null) { this.key2 = key; this.val2 = val; }
        else if(this.key3 == null) { this.key3 = key; this.val3 = val; }
        else {
            if (this.next == null) { this.next = new Options(); }
            this.next.add(key, val);
        }
        return this;
    }

    public Collection<String> getKeys() {
        Collection<String> result = Stream.of(key1, key2, key3)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
        if (this.next != null) {
            result.addAll(this.next.getKeys());
        }
        return result;
    }

    public Options() { }

    public String getKey1() {
        return key1;
    }

    public Options setKey1(String key1) {
        this.key1 = key1;
        return this;
    }

    public Integer getVal1() {
        return val1;
    }

    public Options setVal1(Integer val1) {
        this.val1 = val1;
        return this;
    }

    public String getKey2() {
        return key2;
    }

    public Options setKey2(String key2) {
        this.key2 = key2;
        return this;
    }

    public Integer getVal2() {
        return val2;
    }

    public Options setVal2(Integer val2) {
        this.val2 = val2;
        return this;
    }

    public String getKey3() {
        return key3;
    }

    public Options setKey3(String key3) {
        this.key3 = key3;
        return this;
    }

    public Integer getVal3() {
        return val3;
    }

    public Options setVal3(Integer val3) {
        this.val3 = val3;
        return this;
    }

    public Options getNext() {
        return next;
    }

    public Options setNext(Options next) {
        this.next = next;
        return this;
    }
}