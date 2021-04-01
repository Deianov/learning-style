package bg.geist.service;

import bg.geist.domain.model.service.UserRegistrationModel;

public interface UserService {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    String getFullname(String username);

    void register(UserRegistrationModel model, boolean login);
}
