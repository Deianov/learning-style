package bg.geist.init;

import bg.geist.constant.enums.ModelType;
import bg.geist.domain.model.service.CardsModel;
import bg.geist.domain.model.service.MapModel;
import bg.geist.domain.model.service.QuizModel;
import bg.geist.domain.model.service.QuizSimpleModel;
import bg.geist.init.dto.UserDto;
import bg.geist.repository.*;
import bg.geist.service.CardsService;
import bg.geist.service.MapService;
import bg.geist.service.QuizService;
import bg.geist.service.UserService;
import bg.geist.util.FilesUtil;
import bg.geist.web.api.model.ExerciseIndexModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static bg.geist.constant.Constants.PRJ.*;
import static bg.geist.constant.Constants.INIT.*;

@Component
class MarshalDb {
    private static final ModelType QUIZZES_MODEL_TYPE = ModelType.SIMPLE;

    private final ObjectMapper objectMapper;
    private final QuizRepository quizRepository;
    private final QuizService quizService;
    private final CardsRepository cardsRepository;
    private final CardsService cardsService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MapService mapService;
    private final MapRepository mapRepository;


    MarshalDb(ObjectMapper objectMapper, QuizRepository quizRepository, QuizService quizService, CardsRepository cardsRepository, CardsService cardsService, UserService userService, UserRepository userRepository, CategoryRepository categoryRepository, MapService mapService, MapRepository mapRepository) {
        this.objectMapper = objectMapper;
        this.quizRepository = quizRepository;
        this.quizService = quizService;
        this.cardsRepository = cardsRepository;
        this.cardsService = cardsService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.mapService = mapService;
        this.mapRepository = mapRepository;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    final void prepareDirectories() {

        FilesUtil.delete(new File(RESOURCES_PATH_OUTPUT));
        new File(RESOURCES_PATH_OUTPUT + "\\" + DIRECTORY_NAME_CARDS).mkdirs();
        new File(RESOURCES_PATH_OUTPUT + "\\" + DIRECTORY_NAME_QUIZZES).mkdirs();
        new File(RESOURCES_PATH_OUTPUT + "\\" + DIRECTORY_NAME_MAPS).mkdirs();
    }

    final void marshalUsers() throws IOException {
        // export users
        String filePath = String.format("%s\\users.json", RESOURCES_PATH_OUTPUT);
        Collection<UserDto> users =
                userRepository.findAll()
                        .stream()
                        .filter(user -> !user.getUsername().equals(ADMIN_NAME))
                        .map(userService::userAdministrationModel)
                        .map(user -> new UserDto(
                                user.getId(),
                                user.getUsername(),
                                user.getPassword(),
                                user.getEmail(),
                                user.getFullname(),
                                user.getImageUrl()
                          ))
                        .collect(Collectors.toCollection(ArrayList::new));
        objectMapper.writeValue(new File(filePath), users);
        System.out.printf("marshalUsers: %s%n", filePath);
    }

    final void marshalCategories() throws IOException  {
        String filePath = String.format("%s\\categories.json", RESOURCES_PATH_OUTPUT);
        objectMapper.writeValue(new File(filePath), categoryRepository.findAll());
        System.out.printf("marshalCategories: %s%n", filePath);
    }

    final void marshalCards() throws IOException {
        // export cards index
        String filePath = String.format("%s\\cards.json", RESOURCES_PATH_OUTPUT);
        Collection<ExerciseIndexModel> index = cardsService.getIndex();
        objectMapper.writeValue(new File(filePath), index);
        System.out.printf("marshalCards: %s%n", filePath);

        // export cards
        for (int i = 1; i <= cardsRepository.count(); i++) {
            CardsModel cardsModel = cardsService.getById((long) i);
            filePath = String.format("%s\\%d.json", (RESOURCES_PATH_OUTPUT + "\\" + DIRECTORY_NAME_CARDS), i);
            objectMapper.writeValue(new File(filePath), cardsModel);
            System.out.printf("marshalCards: %s%n", filePath);
        }
    }

    final void marshalQuizzes() throws IOException {
        // export quizzes index
        String filePath = String.format("%s\\quizzes.json", RESOURCES_PATH_OUTPUT);
        Collection<ExerciseIndexModel> index = quizService.getIndex();
        objectMapper.writeValue(new File(filePath), index);
        System.out.printf("marshalQuizzes: %s%n", filePath);

        // export quizzes
        for (int i = 1; i <= quizRepository.count(); i++) {
            filePath = String.format("%s\\%d.json", (RESOURCES_PATH_OUTPUT + "\\" + DIRECTORY_NAME_QUIZZES), i);
            if (QUIZZES_MODEL_TYPE == ModelType.SIMPLE) {
                objectMapper.writeValue(new File(filePath), quizService.getModel((long) i, QuizSimpleModel.class));
            } else {
                objectMapper.writeValue(new File(filePath), quizService.getModel((long) i, QuizModel.class));
            }
            System.out.printf("marshalQuizzes: %s%n", filePath);
        }
    }

    final void marshalMaps() throws IOException {
        // export maps index
        String filePath = String.format("%s\\maps.json", RESOURCES_PATH_OUTPUT);
        Collection<ExerciseIndexModel> index = mapService.getIndex();
        objectMapper.writeValue(new File(filePath), index);
        System.out.printf("marshalMaps: %s%n", filePath);

        // export cards
        for (int i = 1; i <= mapRepository.count(); i++) {
            MapModel mapModel = mapService.getById((long) i);
            filePath = String.format("%s\\%d.json", (RESOURCES_PATH_OUTPUT + "\\" + DIRECTORY_NAME_MAPS), i);
            objectMapper.writeValue(new File(filePath), mapModel);
            System.out.printf("marshalMaps: %s%n", filePath);
        }
    }
}