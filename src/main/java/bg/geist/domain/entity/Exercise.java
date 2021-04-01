package bg.geist.domain.entity;


import bg.geist.domain.entity.enums.ExerciseType;
import bg.geist.domain.entity.enums.Certification;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

// TODO: 15.03.2021 current user


@MappedSuperclass
public abstract class Exercise extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Basic
    private String description;

    @Basic
    private int value;

    @Basic
    private String source;

    @Column(name = "source_url")
    private String sourceUrl;

    @Basic
    private String author;

    @Column(name = "author_url")
    private String authorUrl;

    @Column(name = "time_stamp", nullable = false, updatable = false)
    @CreationTimestamp
    private Date timeStamp;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated", nullable = false)
    @UpdateTimestamp
    private Date updated;

    @Transient
    private ExerciseType type;

    @Column(name = "category_id")
    private long categoryId;

    @OneToOne
    private Options options;

    @Enumerated(value = EnumType.ORDINAL)
    @Basic
    private Certification certification;

    public Integer getOption(String key) {
        return (this.options != null) ? this.options.toMap().get(key) : null;
    }


    public Exercise() { }
    public Exercise(ExerciseType type) {
        this.type = type;
    }
    public Exercise(ExerciseType type, String name, String description, int value){
        this(type);
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Exercise setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Exercise setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getValue() {
        return value;
    }

    public Exercise setValue(int value) {
        this.value = value;
        return this;
    }

    public String getSource() {
        return source;
    }

    public Exercise setSource(String source) {
        this.source = source;
        return this;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public Exercise setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Exercise setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public Exercise setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
        return this;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public Exercise setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Exercise setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Date getUpdated() {
        return updated;
    }

    public Exercise setUpdated(Date updated) {
        this.updated = updated;
        return this;
    }

    public ExerciseType getType() {
        return type;
    }

    public Exercise setType(ExerciseType type) {
        this.type = type;
        return this;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public Exercise setCategoryId(long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public Options getOptions() {
        return options;
    }

    public Exercise setOptions(Options options) {
        this.options = options;
        return this;
    }

    public Certification getCertification() {
        return certification;
    }

    public void setCertification(Certification certification) {
        this.certification = certification;
    }
}