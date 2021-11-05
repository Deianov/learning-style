package bg.geist.init.dto;

import java.util.Collection;

public class UserDto {
    public Long id;
    public String username;
    public String password;
    public String email;
    public String fullname;
    public String imageUrl;

    public UserDto(Long id, String username, String password, String email, String fullname, String imageUrl) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
        this.imageUrl = imageUrl;
    }
}
