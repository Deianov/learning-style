package bg.geist.domain.model.binding;

import bg.geist.domain.model.validators.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static bg.geist.constant.Constants.*;

@FieldMatch(
        first = "password",
        second = "confirmPassword",
        message = PASSWORDS_DOES_NOT_MATCH_MESSAGE
)
public class UserRegistrationBindingModel {
    @NotEmpty
    @Size(min = 4, message = USERNAME_LENGTH_MESSAGE)
    private String username;

    @NotEmpty
    @Email(message = EMAIL_MESSAGE)
    private String email;

    @NotEmpty
    @Size(min = 4, message = FULLNAME_LENGTH_MESSAGE)
    private String fullname;

    @NotEmpty
    @Size(min = 4, max = 20, message = PASSWORD_LENGTH_MESSAGE)
    private String password;

    @NotEmpty
    private String confirmPassword;


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
}
