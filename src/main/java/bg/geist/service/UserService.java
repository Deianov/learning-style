package bg.geist.service;

import bg.geist.domain.model.service.UserProfileModel;
import bg.geist.domain.model.service.UserRegistrationModel;
import bg.geist.domain.model.view.SimpleProfileView;
import org.springframework.security.core.Authentication;


public interface UserService {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    boolean isAuthorized(final Authentication authentication, final Long profileId);
    boolean checkUserPassword(String username, String oldPassword);

    void roleAdd(Long userId, String role);
    void roleRemove(Long userId, String role);

    UserProfileModel profile(Long id);
    SimpleProfileView currentProfile();

    void register(UserRegistrationModel model, boolean login);
    void updateProfile(UserProfileModel model);
}