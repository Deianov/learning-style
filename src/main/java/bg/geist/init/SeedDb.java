package bg.geist.init;

import bg.geist.domain.entity.*;
import bg.geist.domain.entity.Dictionary;
import bg.geist.domain.entity.Map;
import bg.geist.domain.entity.enums.*;
import bg.geist.init.dto.AnswerDto;
import bg.geist.init.dto.CategoryDto;
import bg.geist.init.dto.ExerciseDto;
import bg.geist.init.dto.UserDto;
import bg.geist.init.adapter.ArrayToMatrixAdapter;
import bg.geist.init.dto.*;
import bg.geist.repository.*;
import bg.geist.service.CategoryService;
import bg.geist.service.ExerciseService;
import bg.geist.util.Flags;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

import static bg.geist.constant.Constants.*;
import static bg.geist.constant.Constants.INIT.*;
import static bg.geist.constant.Constants.PRJ.*;

@Component
class SeedDb {
    private static final String JSON_INPUT_PATH = "json/input/";
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserProfileRepository userProfileRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerCollectionRepository answerCollectionRepository;
    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final CardsRepository cardsRepository;
    private final DictionaryCollectionRepository dictionaryCollectionRepository;
    private final DictionaryRepository dictionaryRepository;
    private final MapRepository mapRepository;
    private final PropRepository propRepository;
    private final ExerciseService exerciseService;
    private final ResourceUtils resourceUtils;
    private final ResourceLoader resourceLoader;


    SeedDb(ObjectMapper objectMapper, UserRepository userRepository, UserRoleRepository userRoleRepository, UserProfileRepository userProfileRepository, BCryptPasswordEncoder passwordEncoder, QuizRepository quizRepository, QuestionRepository questionRepository, AnswerCollectionRepository answerCollectionRepository, AnswerRepository answerRepository, CategoryRepository categoryRepository, CategoryService categoryService, CardsRepository cardsRepository, DictionaryCollectionRepository dictionaryCollectionRepository, DictionaryRepository dictionaryRepository, MapRepository mapRepository, PropRepository propRepository, ExerciseService exerciseService, ResourceLoader resourceLoader) {
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answerCollectionRepository = answerCollectionRepository;
        this.answerRepository = answerRepository;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.cardsRepository = cardsRepository;
        this.dictionaryCollectionRepository = dictionaryCollectionRepository;
        this.dictionaryRepository = dictionaryRepository;
        this.mapRepository = mapRepository;
        this.propRepository = propRepository;
        this.exerciseService = exerciseService;
        this.resourceLoader = resourceLoader;
        this.resourceUtils = new ResourceUtils();
    }


    final void readJsonExample() throws IOException {
        // example jackson + TypeReference
        Resource resource = resourceLoader.getResource("classpath:myapp.json");
        try {
            File jsonFile = resource.getFile();
            HashMap<String, String> options = objectMapper
                .readValue(jsonFile, new TypeReference<HashMap<String, String>>() {});
            System.out.printf(" -seedDb: classpath:myapp.json : %s%n", options.get("name"));
        } catch (IOException e) {
            System.out.printf(" -seedDb: %s%n", "unable to read classpath:myapp.json");
        }
    }


    final void seedUsers() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.printf("seedUsers: %s%n", authentication != null ? authentication.getName() : "SecurityContextHolder");

        UserRoleEntity adminRole = new UserRoleEntity().setRole(UserRole.ADMIN);
        UserRoleEntity userRole = new UserRoleEntity().setRole(UserRole.USER);
        userRoleRepository.saveAll(List.of(adminRole, userRole));

        UserEntity admin = new UserEntity(ADMIN_NAME, passwordEncoder.encode(ADMIN_PASSWORD), ADMIN_FULLNAME, ADMIN_EMAIL);
        UserProfile adminProfile = userProfileRepository.save(new UserProfile(null));
        admin.setProfile(adminProfile);
        admin.setRoles(List.of(adminRole, userRole));
        userRepository.saveAll(List.of(admin));

        String resource = resourceUtils.getContent(JSON_INPUT_PATH + "users.json");
        UserDto[] users = objectMapper.readValue(resource, UserDto[].class);

        for (UserDto dto: users) {
            UserEntity user = new UserEntity(dto.username, dto.password, dto.fullname, dto.email);
            UserProfile profile = userProfileRepository.save(new UserProfile(dto.imageUrl));
            user.setProfile(profile);
            user.setRoles(List.of(userRole));
            userRepository.save(user);
        }
        System.out.printf("seedUsers: %s%n", users.length + 1);
    }

    final void seedCategories() throws IOException {
        String resource = resourceUtils.getContent(JSON_INPUT_PATH + "categories.json");
        CategoryDto[] categories = objectMapper.readValue(resource, CategoryDto[].class);

        for (CategoryDto dto : categories) {
            Category category = new Category(dto.name, dto.parentId, dto.sortId);
            category.setId(dto.id);
            categoryRepository.save(category);
        }
        System.out.printf("seedCategories: %s/%s%n", categories.length, categoryRepository.count());
    }

    final void seedCards() throws IOException {
        int value = 1;
        for (Resource resource: resourceUtils.getAll(JSON_INPUT_PATH + DIRECTORY_NAME_CARDS)) {
            JsonNode jsonNode = objectMapper.readTree(resourceUtils.read(resource));
            ExerciseDto exerciseDto = objectMapper.readValue(jsonNode.traverse(), ExerciseDto.class);

            // data
            String[][] data;
            if(exerciseDto.props.containsKey("adapter")) {
                String[] arr = objectMapper.treeToValue(jsonNode.get("data"), String[].class);
                data = ArrayToMatrixAdapter.toMatrix(arr, 2);
            } else {
                data = objectMapper.treeToValue(jsonNode.get("data"), String[][].class);
            }

            // exercise
            Exercise exercise = exerciseService.seed(exerciseService.map(exerciseDto));
            // type
            Cards cards = new Cards(exercise);
            // props
            Set<Prop> props = Prop.fromMap(exerciseDto.props);
            props.forEach(propRepository::save);
            cards.setProps(props);
            // category
            Category category = categoryService.getByName(exerciseDto.exercise.get("category"), ExerciseType.CARDS);
            cards.setCategory(category);
            // dictionaryCollection
            DictionaryCollection dictionaryCollection = dictionaryCollectionRepository
                .save(new DictionaryCollection("cards_" + value++, data.length));
            cards.setDictionaryCollection(dictionaryCollection);
            // dictionaries
            Dictionary dictionary;
            int c = 1;
            for (String[] row : data) {
                dictionary = new Dictionary(c++, row);
                dictionary.setCollection(dictionaryCollection);
                dictionaryCollection.getDictionaries().add(dictionaryRepository.save(dictionary));
            }
            // save
            dictionaryCollectionRepository.save(dictionaryCollection);
            cardsRepository.save(cards);
            System.out.printf("seedCards: %s%n", cards.getExercise().getName());
        }
    }

    final void seedAnswerTemplates() throws IOException {
        String resource = resourceUtils.getContent(JSON_INPUT_PATH + "answers.json");
        AnswerDto[] answers = objectMapper.readValue(resource, AnswerDto[].class);
        HashMap<String, AnswerCollection> collections = new HashMap<>();

        for (AnswerDto dto : answers) {
            AnswerCollection collection = collections.get(dto.collectionName);
            if(collection == null) {
                collection = new AnswerCollection(dto.collectionName);
                int count = Arrays.stream(answers).filter(a -> a.collectionName.equals(dto.collectionName)).map(e -> 1).reduce(0, Integer::sum);
                collection.setValue(count);
                collection = answerCollectionRepository.save(collection);
                collections.put(collection.getName(), collection);
            }
            answerRepository.save(new Answer(dto.text, dto.value, collection));
        }
        System.out.printf("seedAnswerTemplates: %s%n", answers.length);
    }

    final void seedQuizzes() throws IOException, NullPointerException {
        int quizValue = 0;
        String lastCategoryName = "";

        Resource[] resources = resourceUtils.getAll(JSON_INPUT_PATH + DIRECTORY_NAME_QUIZZES);

        for (Resource resource: resources) {
            String content = resourceUtils.read(resource);

            // unmarshal from json
            QuizDto quizDto = objectMapper.readValue(content, QuizDto.class);

            // exercise
            ExerciseDto exerciseDto = objectMapper.readValue(objectMapper.readTree(content).traverse(), ExerciseDto.class);
            Exercise exercise = exerciseService.seed(exerciseService.map(exerciseDto));

            // category
            String categoryName = exerciseDto.exercise.get("category");
            Category category = categoryService.getByName(categoryName, ExerciseType.QUIZZES);

            // sort value
            quizValue = categoryName.equals(lastCategoryName) ? quizValue + 1 : 1;
            lastCategoryName = categoryName;

            // quiz
            Quiz quiz = new Quiz(exercise);
            quiz.setCategory(category);

            // props
            if (exerciseDto.props != null && exerciseDto.props.containsKey(QUIZZES_CERTIFICATION_KEY)) {
                // certification (Certification.NONE, ON_SERVER; ON_SERVER_STRICT)
                quiz.setCertification(Certification.byOrdinal(Integer.parseInt(exerciseDto.props.get(QUIZZES_CERTIFICATION_KEY))));
//                exerciseDto.props.remove(QUIZZES_CERTIFICATION_KEY);
            } else {
                quiz.setCertification(QUIZZES_CERTIFICATION);
            }
            quiz.setLang(Lang.valueOf(exerciseDto.props.remove("lang")));
            quiz.setLevel(Level.valueOf(exerciseDto.props.remove("level")));
            Set<Prop> props = Prop.fromMap(exerciseDto.props);
            props.forEach(propRepository::save);
            quiz.setProps(props);


            // questions
            Integer[] correct = quizDto.correct.toArray(Integer[]::new);
            int count = 0;
            for (QuestionDto questionDto : quizDto.questions) {
                int flags = correct[count];
                Question question = new Question(questionDto.text, count++);

                // correct, flags and type
                question.setCorrect(flags);
                if (questionDto.answers.size() == 2) {
                    question.setType(Question.Type.BOOLEAN);
                } else {
                    question.setType(Flags.isSingle(flags) ? Question.Type.SINGLE : Question.Type.MULTIPLE);
                }

                // answers
                createAnswers(question, questionDto.answers);
                questionRepository.save(question);
                quiz.getQuestions().add(question);
            }
            // save
            quizRepository.save(quiz);
            System.out.printf("seedQuizzes: %s%n", exercise.getName());
        }
    }

    void createAnswers (Question question, Collection<AnswerDto> answerDtos) {
        String[] answers = answerDtos.stream().map(answerDto -> answerDto.text).toArray(String[]::new);

        if (question.getType() == Question.Type.BOOLEAN) {
            question.setAnswersCollection(answerCollectionRepository.getOne(1L));
        } else {
            AnswerCollection answerCollection = new AnswerCollection();
            answerCollection.setValue(answers.length);
            answerCollectionRepository.save(answerCollection);
            question.setAnswersCollection(answerCollection);

            for (int i = 0; i < answers.length; i++) {
                answerRepository.save(new Answer(answers[i], Flags.toBits(i), answerCollection));
            }
        }
    }

    final void seedMaps () throws IOException {
        Resource[] resources = resourceUtils.getAll(JSON_INPUT_PATH + DIRECTORY_NAME_MAPS);

        for (Resource resource: resources) {

            JsonNode jsonNode = objectMapper.readTree(resourceUtils.read(resource));

            // exercise
            ExerciseDto exerciseDto = objectMapper.readValue(jsonNode.traverse(), ExerciseDto.class);
            Exercise exercise = exerciseService.seed(exerciseService.map(exerciseDto));
            // props
            Set<Prop> props = Prop.fromMap(exerciseDto.props);
            props.forEach(propRepository::save);
            // map
            Map map = new Map(exercise);
            map.setProps(props);
            map.setCategory(categoryService.getByName(exerciseDto.exercise.get("category"), ExerciseType.MAPS));
            map.setCreatedBy(ADMIN_NAME);
            mapRepository.save(map);

            System.out.printf("seedMaps: %s%n", exercise.getName());
        }
    }
}