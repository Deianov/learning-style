package bg.geist.web.controller;

import bg.geist.constant.Constants;
import bg.geist.constant.enums.CloudinaryTags;
import bg.geist.domain.model.binding.UserRegistrationBindingModel;
import bg.geist.domain.model.service.UserRegistrationModel;
import bg.geist.exception.FieldName;
import bg.geist.service.CloudinaryService;
import bg.geist.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;


@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String USERNAME_KEY = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;
    private static final String BAD_CREDENTIALS_KEY = "bad_credentials";
    private static final String REGISTRATION_BINDING_MODEL = "registrationBindingModel";
    private static final String REGISTRATION_BINDING_MODEL_RESULTS = "org.springframework.validation.BindingResult." + REGISTRATION_BINDING_MODEL;


    private final UserService userService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, CloudinaryService cloudinaryService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login-error")
    public String loginError(@ModelAttribute(USERNAME_KEY) String username,
                             RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(BAD_CREDENTIALS_KEY, true);
        redirectAttributes.addFlashAttribute(USERNAME_KEY, username);
        return "redirect:/users/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        if(!model.containsAttribute(REGISTRATION_BINDING_MODEL)) {
            model.addAttribute(REGISTRATION_BINDING_MODEL, new UserRegistrationBindingModel());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerForm(@Valid final UserRegistrationBindingModel bindingModel,
                               final BindingResult result,
                               final RedirectAttributes redirectAttributes) throws IOException {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(REGISTRATION_BINDING_MODEL, bindingModel);
            redirectAttributes.addFlashAttribute(REGISTRATION_BINDING_MODEL_RESULTS, result);
            return "redirect:/users/register";
        }

        boolean hasErrors = false;

        if (userService.existsByUsername(bindingModel.getUsername())) {
            hasErrors = true;
            result.rejectValue(FieldName.username.toString(), "error." + REGISTRATION_BINDING_MODEL,
                    Constants.USERNAME_EXISTS_MESSAGE);
        }

        if (userService.existsByEmail(bindingModel.getEmail())) {
            hasErrors = true;
            result.rejectValue(FieldName.email.toString(), "error." + REGISTRATION_BINDING_MODEL,
                    Constants.EMAIL_EXISTS_MASSAGE);
        }

        if (hasErrors) {
            redirectAttributes.addFlashAttribute(REGISTRATION_BINDING_MODEL, bindingModel);
            redirectAttributes.addFlashAttribute(REGISTRATION_BINDING_MODEL_RESULTS, result);
            return "redirect:/users/register";
        }

        UserRegistrationModel registrationModel = modelMapper.map(bindingModel, UserRegistrationModel.class);
        registrationModel.setImageUrl(cloudinaryService.uploadImage(
                bindingModel.getImage(),
                bindingModel.getUsername(),
                CloudinaryTags.avatar)
                        .getUrl());
        userService.register(registrationModel, true);
        return "redirect:/home";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails principal, Model model){
        // todo : add profile with interceptor
        model.addAttribute("profile", userService.profile(principal.getUsername()));
        return "profile";
    }
}