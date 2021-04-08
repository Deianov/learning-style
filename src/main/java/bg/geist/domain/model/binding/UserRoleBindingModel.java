package bg.geist.domain.model.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class UserRoleBindingModel {
    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    private String role;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
