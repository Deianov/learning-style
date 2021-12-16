package bg.geist.domain.entity;

import bg.geist.domain.entity.enums.Certification;
import bg.geist.domain.entity.enums.ExerciseType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@MappedSuperclass
public abstract class ExercisePlay {
    @Transient
    private ExerciseType type;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @OneToOne
    private Category category;

    // hibernate only annotation to ensure second eager
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    private Set<Prop> props = new HashSet<>();

    @Column(name = "time_stamp", nullable = false, updatable = false)
    @CreationTimestamp
    private Date timeStamp;

    @Column(name = "updated", nullable = false)
    @UpdateTimestamp
    private Date updated;

    @Column(name = "created_by")
    private String createdBy;

    @Enumerated(value = EnumType.ORDINAL)
    @Basic
    private Certification certification;


    public ExercisePlay() { }
    public ExercisePlay(ExerciseType type) {
        this.type = type;
    }


    public ExerciseType getType() {
        return type;
    }
    public void setType(ExerciseType type) {
        this.type = type;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public Set<Prop> getProps() {
        return props;
    }
    public void setProps(Set<Prop> props) {
        this.props = props;
    }
    public Date getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    public Date getUpdated() {
        return updated;
    }
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public Certification getCertification() {
        return certification;
    }
    public void setCertification(Certification certification) {
        this.certification = certification;
    }
}