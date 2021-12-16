package bg.geist.domain.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Basic
    private String description;

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

    @Column(name = "updated", nullable = false)
    @UpdateTimestamp
    private Date updated;

    @Column(name = "created_by")
    private String createdBy;

    public Exercise() {};
    public Exercise(String name, String description, String source, String sourceUrl, String author, String authorUrl, String createdBy) {
        this.name = name;
        this.description = description;
        this.source = source;
        this.sourceUrl = sourceUrl;
        this.author = author;
        this.authorUrl = authorUrl;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return name.equals(exercise.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
