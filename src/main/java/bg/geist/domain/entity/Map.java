package bg.geist.domain.entity;

import bg.geist.domain.entity.enums.ExerciseType;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "maps")
public class Map extends Exercise {
    public Map() {
        super(ExerciseType.MAPS);
    }
}
