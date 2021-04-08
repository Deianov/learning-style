package bg.geist.domain.entity;

import bg.geist.domain.entity.enums.UserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity{

    @Column(nullable = false, unique = true, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne(optional = false)
    private UserProfile profile;


    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<UserRoleEntity> roles = new ArrayList<>();

    public UserEntity() { }
    public UserEntity(String username, String password, String fullname, String email) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public Collection<UserRoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Collection<UserRoleEntity> roles) {
        this.roles = roles;
    }

    public UserEntity addRole(UserRoleEntity roleEntity) {
        this.roles.add(roleEntity);
        return this;
    }

    public boolean hasRole(UserRole role) {
        if (role == null) {
            return false;
        }
        for (UserRoleEntity roleEntity : roles) {
            if (roleEntity.getRole() == role) {
                return true;
            }
        }
        return false;
    }
}
