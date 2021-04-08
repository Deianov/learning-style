package bg.geist.domain.model.binding;

import bg.geist.constant.Constants;
import bg.geist.domain.model.validators.FieldMatch;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@FieldMatch(
        first = "password",
        second = "confirmPassword",
        message = "{password.match}"
)
public class UserProfileBindingModel {
    private String username;
    private String email;

    @NotEmpty
    @Size(min = Constants.FULLNAME_MIN_LENGTH, message = "{fullname.length.min}")
    private String fullname;
    private String oldPassword;

    @Pattern(regexp = Constants.NEW_PASSWORDS_REGEX, message = Constants.PASSWORDS_LENGTH_MESSAGE)
    private String password;
    private String confirmPassword;
    private String imageUrl;
    private MultipartFile image;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}