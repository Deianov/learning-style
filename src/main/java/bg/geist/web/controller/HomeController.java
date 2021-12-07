package bg.geist.web.controller;

import bg.geist.constant.Constants;
import bg.geist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
public class HomeController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String index(@AuthenticationPrincipal final UserDetails principal) {
        if (principal != null) {
            return "redirect:/home?username=" + principal.getUsername();
        }
        return "index";
    }

    @GetMapping("/home")
    public String home(HttpSession session) {
        session.setAttribute(Constants.PROFILE_KEY, userService.currentProfile());
        return "home";
    }
}