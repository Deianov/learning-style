package bg.geist.domain.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tags")
public class Tag extends BaseEntity{

    @Basic
    private String text;

    @Basic
    private int value;
}