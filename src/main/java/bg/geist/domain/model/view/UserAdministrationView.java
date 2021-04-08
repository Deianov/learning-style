package bg.geist.domain.model.view;


import java.util.Collection;

public final class UserAdministrationView {
    private final Long id;
    private final String username;
    private final String email;
    private final String fullname;
    private final String imageUrl;
    private final Long profile;
    private final Collection<String> roles;
    private final boolean isAdmin;


    public UserAdministrationView(Long id, String username, String email, String fullname, String imageUrl, Long profile, Collection<String> roles, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.imageUrl = imageUrl;
        this.profile = profile;
        this.roles = roles;
        this.isAdmin = isAdmin;
    }

    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
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