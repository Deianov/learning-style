package bg.geist.web.api.quiz;

import bg.geist.service.QuizService;
import bg.geist.web.api.exercise.ExerciseIndexModel;
import bg.geist.web.api.quiz.models.QuizValidationRequestModel;
import bg.geist.web.api.quiz.models.QuizValidationResponseModel;
import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping(path="/api/quiz", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuizController {
    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);
    private static final String VALIDATED_QUIZ = "Quiz was validated, id:{}";

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public ResponseEntity<Collection<ExerciseIndexModel>> getIndex(
            @RequestHeader(value = "accept-language", required = false) String language) {
        // TODO: 25.03.2021 lang
        logger.info("accept-language: {}", language);
        Collection<ExerciseIndexModel> index = quizService.getIndex();
        if(index.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(index, HttpStatus.OK);
    }

    /*
        // send correct answers if there is not server validation
        if (ExerciseValidation.byOrdinal(map.get("validation")) != ExerciseValidation.NONE) {
            quizModel.setCorrect(null);
        }
    */

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Object getQuiz(@PathVariable Long id) {
        return quizService.getResponseModel(id);
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<QuizValidationResponseModel> validate(
            @NotNull @PathVariable Long id,
            @RequestBody QuizValidationRequestModel validationModel) {
        QuizValidationResponseModel responseModel = quizService.validate(validationModel.setQuizId(id));
        logger.info(VALIDATED_QUIZ, id);
        return new ResponseEntity<>(responseModel, HttpStatus.ACCEPTED);
    }
}