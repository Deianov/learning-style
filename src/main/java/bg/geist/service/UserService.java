package bg.geist.service;

import bg.geist.domain.model.service.UserRegistrationModel;
import bg.geist.domain.model.view.SimpleProfileView;

public interface UserService {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    SimpleProfileView profile(String username);
    void register(UserRegistrationModel model, boolean login);
}
