package bg.geist.web.controller;

import com.sun.istack.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ExerciseController {
    // single page redirects (by page/router.js)
    private static final String REDIRECT_STRING = "redirect:/?page=%s";
    private static final String REDIRECT_WITH_ID_STRING = "redirect:/?page=%s&id=%d";


    @GetMapping("/cards")
    public String cards(@RequestParam(required = false) Long id) {
        if(id != null) {
            return String.format(REDIRECT_WITH_ID_STRING, "cards", id);
        }
        return String.format(REDIRECT_STRING, "cards");
    }

    @GetMapping("/cards/{id}")
    public String cardsId(@NotNull @PathVariable Long id) {
        return String.format(REDIRECT_WITH_ID_STRING, "cards", id);
    }

    @GetMapping("/quiz")
    public String quiz(@RequestParam(required = false) Long id) {
        if(id != null) {
            return String.format(REDIRECT_WITH_ID_STRING, "quiz", id);
        }
        return String.format(REDIRECT_STRING, "quiz");
    }

    @GetMapping("/quiz/{id}")
    public String quizId(@NotNull @PathVariable Long id) {
        return String.format(REDIRECT_WITH_ID_STRING, "quiz", id);
    }
}