package bg.geist.web.controller;

import com.sun.istack.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ExerciseController {
    // single page redirects (by page/router.js)
    private static final String PAGE_CARDS = "1";
    private static final String PAGE_QUIZZES = "2";
    private static final String PAGE_MAPS = "3";
    private static final String REDIRECT_PAGE = "redirect:/home?page=%s";
    private static final String REDIRECT_ID = "redirect:/home?page=%s&id=%d";


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


    @GetMapping("/quizzes")
    public String quiz(@RequestParam(required = false) Long id) {
        if(id != null) {
            return String.format(REDIRECT_ID, PAGE_QUIZZES, id);
        }
        return String.format(REDIRECT_PAGE, PAGE_QUIZZES);
    }

    @GetMapping("/quizzes/{id}")
    public String quizId(@NotNull @PathVariable Long id) {
        return String.format(REDIRECT_ID, PAGE_QUIZZES, id);
    }


    @GetMapping("/maps")
    public String maps(@RequestParam(required = false) Long id) {
        if(id != null) {
            return String.format(REDIRECT_ID, PAGE_MAPS, id);
        }
        return String.format(REDIRECT_PAGE, PAGE_MAPS);
    }


    @GetMapping("/maps/{id}")
    public String mapsId(@NotNull @PathVariable Long id) {
        return String.format(REDIRECT_ID, PAGE_MAPS, id);
    }
}