package bg.geist.web.api.quizzes;

import bg.geist.service.QuizService;
import bg.geist.web.api.exercise.ExerciseIndexModel;
import bg.geist.web.api.quizzes.models.QuizCertificationRequestModel;
import bg.geist.web.api.quizzes.models.QuizCertificationResponseModel;
import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping(path="/api/quizzes", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuizController {
    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);
    private static final String CERTIFICATED_QUIZ = "Quiz was certificated, id:{}";

    private final QuizService quizService;

    @Autowired
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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Object getQuiz(@PathVariable Long id) {
        return quizService.getResponseModel(id);
    }

    @PostMapping("/{id}/certification")
    public ResponseEntity<QuizCertificationResponseModel> certification(
            @NotNull @PathVariable Long id,
            @RequestBody QuizCertificationRequestModel requestModel) {
        QuizCertificationResponseModel response = quizService.certificate(requestModel.setQuizId(id));
        logger.info(CERTIFICATED_QUIZ, id);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}