package bg.geist.domain.model.service;

import java.util.Collection;
import java.util.Collections;

public class UserAdministrationModel {
    private final Long id;
    private final String username;
    private final String password;
    private final String email;
    private final String fullname;
    private final String imageUrl;
    private final Long profile;
    private final Collection<String> roles;
    private final boolean isAdmin;

    public UserAdministrationModel(Long id, String username, String password, String email, String fullname, String imageUrl, Long profile, Collection<String> roles, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
        this.imageUrl = imageUrl;
        this.profile = profile;
        this.roles = Collections.unmodifiableCollection(roles);
        this.isAdmin = isAdmin;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getProfile() {
        return profile;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}