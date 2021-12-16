package bg.geist.domain.entity;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "props")
public class Prop{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "str_key")
    private String key;

    @Column(name = "str_value")
    private String value;

    @Column
    private int flag = 0;


    public static HashMap<String, String> toMap(Collection<Prop> props) {
        HashMap<String, String> result = new HashMap<>();
        props.forEach(prop -> result.put(prop.getKey(), prop.getValue()));
        return result;
    }

    public static HashMap<String, String> toMap(Set<Prop> props) {
        HashMap<String, String> result = new HashMap<>();
        props.forEach(prop -> result.put(prop.getKey(), prop.getValue()));
        return result;
    }

    public static Set<Prop> fromMap(HashMap<String, String> map) {
        Set<Prop> props = new HashSet<>();
        if(map != null) {
            map.forEach((key1, value1) -> props.add(new Prop(key1, value1)));
        }
        return props;
    }


    public Prop() {}
    public Prop(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public Prop(String key, String value, Integer flag) {
        this(key, value);
        this.flag = flag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prop prop = (Prop) o;
        return key.equals(prop.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}