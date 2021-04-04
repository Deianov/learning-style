package bg.geist.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users_profiles")
public class UserProfile extends BaseEntity{

    @Column(name = "image_url")
    private String imageUrl;

    public UserProfile() {}
    public UserProfile(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

}
