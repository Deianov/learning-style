package bg.geist.web.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static bg.geist.constant.Constants.INIT.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(username = USER_NAME, authorities = {"USER"})
    void getUsersViewWithUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/admin/users").with(csrf()))
                .andExpect(status().isFound());
    }

//    @Test
//    @WithMockUser(username = ADMIN_NAME, authorities = {"USER", "ADMIN"})
//    void getUsersViewWithAdmin() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .get("/admin/users").with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("admin-users"));
//    }

/*
    void init() {
        String ADMIN_NAME = "admin";
        String ADMIN_PASSWORD = "admin";
        String ADMIN_FULLNAME = "TEST ADMINISTRATOR";
        String ADMIN_EMAIL = "admin@admin.com";

        String USER_NAME = "user";
        String USER_PASSWORD = "user";
        String USER_FULLNAME = "TEST USER";
        String USER_EMAIL = "user@user.com";
        String USER_IMAGE = "https://res.cloudinary.com/deianov/image/upload/v1617366058/user.jpg";

        UserRoleEntity adminRole = new UserRoleEntity().setRole(UserRole.ADMIN);
        UserRoleEntity userRole = new UserRoleEntity().setRole(UserRole.USER);
        userRoleRepository.saveAll(List.of(adminRole, userRole));

        UserEntity admin = new UserEntity(ADMIN_NAME, passwordEncoder.encode(ADMIN_PASSWORD), ADMIN_FULLNAME, ADMIN_EMAIL);
        UserProfile adminProfile = userProfileRepository.save(new UserProfile(null));
        admin.setProfile(adminProfile);
        admin.setRoles(List.of(adminRole, userRole));

        UserEntity user = new UserEntity(USER_NAME, passwordEncoder.encode(USER_PASSWORD), USER_FULLNAME, USER_EMAIL);
        UserProfile userProfile = userProfileRepository.save(new UserProfile(USER_IMAGE));
        user.setProfile(userProfile);
        user.setRoles(List.of(userRole));
        userRepository.saveAll(List.of(admin, user));
    }
 */
}