package bg.geist.web.controller;

import com.sun.istack.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ExerciseController {
    // single page redirects (by page/router.js)
    private static final String PAGE_CARDS = "cards";
    private static final String PAGE_QUIZZES = "quiz";
    private static final String PAGE_GAMES = "games";
    private static final String REDIRECT_PAGE = "redirect:/?page=%s";
    private static final String REDIRECT_ID = "redirect:/?page=%s&id=%d";


    @GetMapping("/cards")
    public String cards(@RequestParam(required = false) Long id) {
        if(id != null) {
            return String.format(REDIRECT_ID, PAGE_CARDS, id);
        }
        return String.format(REDIRECT_PAGE, PAGE_CARDS);
    }

    @GetMapping("/cards/{id}")
    public String cardsId(@NotNull @PathVariable Long id) {
        return String.format(REDIRECT_ID, PAGE_CARDS, id);
    }


    @GetMapping("/quiz")
    public String quiz(@RequestParam(required = false) Long id) {
        if(id != null) {
            return String.format(REDIRECT_ID, PAGE_QUIZZES, id);
        }
        return String.format(REDIRECT_PAGE, PAGE_QUIZZES);
    }

    @GetMapping("/quiz/{id}")
    public String quizId(@NotNull @PathVariable Long id) {
        return String.format(REDIRECT_ID, PAGE_QUIZZES, id);
    }


    @GetMapping("/games")
    public String games(@RequestParam(required = false) Long id) {
        if(id != null) {
            return String.format(REDIRECT_ID, PAGE_GAMES, id);
        }
        return String.format(REDIRECT_PAGE, PAGE_GAMES);
    }
}