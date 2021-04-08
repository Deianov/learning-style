package bg.geist.config;

import bg.geist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public final class Guard {
    private final UserService userService;

    @Autowired
    public Guard(final UserService userService) {
        this.userService = userService;
    }

    /**
     * @param authentication request authentication
     * @param id user profile id
     * @return authorisation to view the profile
     */
    public final boolean isAuthorized(final Authentication authentication, final Long id) {
        return userService.isAuthorized(authentication, id);
    }
}