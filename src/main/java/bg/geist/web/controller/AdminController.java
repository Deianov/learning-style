package bg.geist.web.controller;

import bg.geist.constant.Constants;
import bg.geist.domain.entity.enums.UserRole;
import bg.geist.domain.model.binding.UserRoleBindingModel;
import bg.geist.domain.model.view.SimpleProfileView;
import bg.geist.domain.model.view.UserAdministrationView;
import bg.geist.repository.UserRepository;
import bg.geist.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public final class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static final String PAGE_HOME = "admin/admin-panel";
    private static final String PAGE_USERS = "admin/admin-users";
    private static final String REDIRECT_USERS = "redirect:/admin/users";

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public AdminController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @ModelAttribute(Constants.PROFILE_KEY)
    public final SimpleProfileView profile() {
        return userService.currentProfile();
    }

    @GetMapping
    public final String home(){ return PAGE_HOME; }

    @GetMapping("/users")
    public final ModelAndView users(){
        Collection<UserAdministrationView> users =
                userRepository.findAll()
                        .stream().map(user -> new UserAdministrationView(
                                user.getId(),
                                user.getUsername(),
                                user.getEmail(),
                                user.getFullname(),
                                user.getProfile().getImageUrl(),
                                user.getProfile().getId(),
                                user.getRoles().stream().map(r -> r.getRole().toString()).collect(Collectors.toList()),
                                user.hasRole(UserRole.ADMIN)
                        ))
                        .collect(Collectors.toCollection(ArrayList::new));
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", users);
        mav.addObject("userRoleBindingModel", new UserRoleBindingModel());
        mav.setViewName(PAGE_USERS);
        return mav;
    }

    @RequestMapping(path = "/users/role-add", method = {RequestMethod.PATCH, RequestMethod.POST})
    public final String roleAdd(@NotNull @Valid final UserRoleBindingModel bindingModel,
                                final Errors errors) {
        if (errors.hasErrors()) {
            throw new BadCredentialsException(Constants.USER_BAD_CREDENTIALS);
        }
        userService.roleAdd(bindingModel.getId(), bindingModel.getRole());
        return REDIRECT_USERS;
    }

    @RequestMapping(path = "/users/role-remove", method = {RequestMethod.DELETE, RequestMethod.POST})
    public final String roleRemove(@NotNull @Valid final UserRoleBindingModel bindingModel,
                                   final Errors errors) {
        if (errors.hasErrors()) {
            throw new BadCredentialsException(Constants.USER_BAD_CREDENTIALS);
        }
        userService.roleRemove(bindingModel.getId(), bindingModel.getRole());
        return REDIRECT_USERS;
    }
}