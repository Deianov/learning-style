package bg.geist.domain.model.binding;

import bg.geist.constant.Constants;
import bg.geist.domain.model.validators.FieldMatch;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@FieldMatch(
        first = "password",
        second = "confirmPassword",
        message = "{password.match}"
)
public class UserRegistrationBindingModel {
    @NotEmpty
    @Size(min = Constants.USERNAME_MIN_LENGTH, message = "{username.length.min}")
    private String username;

    @NotEmpty
    @Email(message = "{email.invalid}")
    private String email;

    @NotEmpty
    @Size(min = Constants.FULLNAME_MIN_LENGTH, message = "{fullname.length.min}")
    private String fullname;

    @NotEmpty
    @Size(min = Constants.PASSWORD_MIN_LENGTH, max = Constants.PASSWORD_MAX_LENGTH, message = "{password.length}")
    private String password;

    @NotEmpty
    private String confirmPassword;

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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}