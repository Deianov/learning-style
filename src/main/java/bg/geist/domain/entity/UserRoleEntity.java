package bg.geist.domain.entity;


import bg.geist.domain.entity.enums.UserRole;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class UserRoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserRole getRole() {
        return role;
    }

    public UserRoleEntity setRole(UserRole role) {
        this.role = role;
        return this;
    }
}