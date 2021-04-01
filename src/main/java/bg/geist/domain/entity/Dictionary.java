package bg.geist.domain.entity;

import bg.geist.domain.entity.enums.Lang;
import bg.geist.domain.entity.enums.Level;
import bg.geist.domain.entity.enums.TextRole;
import bg.geist.domain.entity.enums.TextType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;
import java.util.stream.Stream;

@Entity
@Table(name = "dictionary")
public class Dictionary extends BaseEntity {
    @Column
    private int value = 0;

    @Column
    private String att1;

    @Column
    private String att2;

    @Column
    private String att3;

    @Enumerated(EnumType.STRING)
    @Column
    private Lang lang1;

    @Enumerated(EnumType.STRING)
    @Column
    private Lang lang2;

    @Enumerated(EnumType.STRING)
    @Column
    private TextType type;

    @Enumerated(EnumType.STRING)
    @Column
    private Level level;

    @Enumerated(EnumType.STRING)
    @Column
    private TextRole role2;

    @Enumerated(EnumType.STRING)
    @Column
    private TextRole role3;

    @JsonIgnore
    @ManyToOne
    private DictionaryCollection collection;


    public String[] toArray() {
        return Stream.of(att1, att2, att3).filter(Objects::nonNull).toArray(String[]::new);
    }

    public int getValue() {
        return value;
    }

    public Dictionary setValue(int value) {
        this.value = value;
        return this;
    }

    public String getAtt1() {
        return att1;
    }

    public Dictionary setAtt1(String att1) {
        this.att1 = att1;
        return this;
    }

    public String getAtt2() {
        return att2;
    }

    public Dictionary setAtt2(String att2) {
        this.att2 = att2;
        return this;
    }

    public String getAtt3() {
        return att3;
    }

    public Dictionary setAtt3(String att3) {
        this.att3 = att3;
        return this;
    }

    public Lang getLang1() {
        return lang1;
    }

    public Dictionary setLang1(Lang lang1) {
        this.lang1 = lang1;
        return this;
    }

    public Lang getLang2() {
        return lang2;
    }

    public Dictionary setLang2(Lang lang2) {
        this.lang2 = lang2;
        return this;
    }

    public TextType getType() {
        return type;
    }

    public Dictionary setType(TextType type) {
        this.type = type;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    public Dictionary setLevel(Level level) {
        this.level = level;
        return this;
    }

    public TextRole getRole2() {
        return role2;
    }

    public Dictionary setRole2(TextRole role2) {
        this.role2 = role2;
        return this;
    }

    public TextRole getRole3() {
        return role3;
    }

    public Dictionary setRole3(TextRole role3) {
        this.role3 = role3;
        return this;
    }

    public DictionaryCollection getCollection() {
        return collection;
    }

    public Dictionary setCollection(DictionaryCollection collection) {
        this.collection = collection;
        return this;
    }
}