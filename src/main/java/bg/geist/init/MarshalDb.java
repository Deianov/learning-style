package bg.geist.init;

import bg.geist.domain.enums.ModelType;
import bg.geist.domain.model.CardsModel;
import bg.geist.domain.model.QuizModel;
import bg.geist.domain.model.QuizSimpleModel;
import bg.geist.repository.CardsRepository;
import bg.geist.repository.QuizRepository;
import bg.geist.service.CardsService;
import bg.geist.service.QuizService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class MarshalDb {
    private static final String RESOURCES_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\";
    private static final String path1 = RESOURCES_PATH + "output";
    private static final String path2 = path1 + "\\cards";
    private static final String path3 = path1 + "\\quiz";
    private static final ModelType QUIT_MODEL_TYPE = ModelType.SIMPLE;

    private final ObjectMapper objectMapper;
    private final QuizRepository quizRepository;
    private final QuizService quizService;
    private final CardsRepository cardsRepository;
    private final CardsService cardsService;


    public MarshalDb(ObjectMapper objectMapper, QuizRepository quizRepository, QuizService quizService, CardsRepository cardsRepository, CardsService cardsService) {
        this.objectMapper = objectMapper;
        this.quizRepository = quizRepository;
        this.quizService = quizService;
        this.cardsRepository = cardsRepository;
        this.cardsService = cardsService;
    }

    public void createDirectories() {

        File parent = new File(path1);
        File dirCards = new File(path2);
        File dirQuiz = new File(path3);

        if (!parent.exists()) {
           if (parent.mkdirs()) {
               dirCards.mkdirs();
               dirQuiz.mkdirs();
           }
        }
    }

    public void marshalCards() throws IOException {
        for (int i = 1; i <= cardsRepository.count(); i++) {
            CardsModel cardsModel = cardsService.getById((long) i);
            String filePath = String.format("%s\\%d.json", path2, i);
            objectMapper.writeValue(new File(filePath), cardsModel);
            System.out.printf("marshal: %s%n", filePath);
        }
    }

    public void marshalQuizzes() throws IOException {

        for (int i = 1; i <= quizRepository.count(); i++) {
//            QuizSimpleModel quizModel = quizService.getModel((long) i, QuizSimpleModel.class);
            String filePath = String.format("%s\\%d.json", path3, i);
            if (QUIT_MODEL_TYPE == ModelType.SIMPLE) {
                objectMapper.writeValue(new File(filePath), quizService.getModel((long) i, QuizSimpleModel.class));
            } else {
                objectMapper.writeValue(new File(filePath), quizService.getModel((long) i, QuizModel.class));
            }
            System.out.printf("marshal: %s%n", filePath);
        }
    }
}