package bg.geist.web.controller;

import bg.geist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class HomeController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String index(@AuthenticationPrincipal UserDetails principal) {
        if (principal != null) {
            return "redirect:/home";
        }
        return "index";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UserDetails principal, Model model) {
        model.addAttribute("profile", userService.profile(principal.getUsername()));
        return "home";
    }
}