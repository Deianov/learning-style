package bg.geist.domain.entity;


import javax.persistence.*;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(nullable = false, length = 30)
    private String name;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "sort_id")
    private int sortId;


    public Category() {}
    public Category(String name) {
        this.name = name;
    }
    public Category(String name, Long parentId, int sortId) {
        this.name = name;
        this.parentId = parentId;
        this.sortId = sortId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }
}