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
import bg.geist.util.FilesUtil;
import bg.geist.util.Flags;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static bg.geist.constant.Constants.*;
import static bg.geist.constant.Constants.INIT.*;
import static bg.geist.constant.Constants.PRJ.*;

@Component
class SeedDb {
    private static final String JSON_INPUT_PATH = "json/input/";


    private final Resource jsonFile;
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


    SeedDb(@Value("classpath:myapp.json") Resource jsonFile, ObjectMapper objectMapper, UserRepository userRepository, UserRoleRepository userRoleRepository, UserProfileRepository userProfileRepository, BCryptPasswordEncoder passwordEncoder, QuizRepository quizRepository, QuestionRepository questionRepository, AnswerCollectionRepository answerCollectionRepository, AnswerRepository answerRepository, CategoryRepository categoryRepository, CategoryService categoryService, CardsRepository cardsRepository, DictionaryCollectionRepository dictionaryCollectionRepository, DictionaryRepository dictionaryRepository, MapRepository mapRepository, PropRepository propRepository, ExerciseService exerciseService) {
        this.jsonFile = jsonFile;
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
    }

    final void showVersion() throws IOException {
        /* example constructor: @Value("classpath:myapp.json") Resource jsonFile */
        System.out.println(jsonFile.getFile().toString());
        /* example jackson + TypeReference */
        HashMap<String, String> options = objectMapper
                .readValue(jsonFile.getFile(), new TypeReference<HashMap<String, String>>() {});
        System.out.printf("showVersion: %s%n", options.get("version"));
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

        Resource resource = getResource("users.json");
        UserDto[] users = objectMapper.readValue(resource.getFile(), UserDto[].class);

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
        Resource resource = getResource("categories.json");
        CategoryDto[] categories = objectMapper.readValue(resource.getFile(), CategoryDto[].class);

        for (CategoryDto dto : categories) {
            Category category = new Category(dto.name, dto.parentId, dto.sortId);
            category.setId(dto.id);
            categoryRepository.save(category);
        }
        System.out.printf("seedCategories: %s%n", categories.length);
    }

    private int CompareNames(File f1, File f2) {
        if (f1 == null || f2 == null) {
            return 0;
        }
        String a = f1.getName();
        String b = f2.getName();
        if (a.matches(JSON_FILENAME_PATTERN) && b.matches(JSON_FILENAME_PATTERN)) {
            int ai = Integer.parseInt(a.substring(0, a.lastIndexOf(".")));
            int bi = Integer.parseInt(b.substring(0, b.lastIndexOf(".")));
            return ai - bi;
        } else {
            return a.compareTo(b);
        }
    }

    /* resources example: files (absolute path) */
    private File[] getFiles(String directoryName) {
        final String path = RESOURCES_PATH_INPUT + "\\" + directoryName;
        return Arrays.stream(FilesUtil.listFiles(path))
            .filter(File::isFile)
            .sorted(this::CompareNames)
            .toArray(File[]::new);
    }

    /* resources example: ClassPathResource (relative path) */
    private Resource getResource(String filePath) {
        final String path = JSON_INPUT_PATH + filePath;
        return new ClassPathResource(path);
    }

    /* resources example: ClassPathResources (relative path) */
    private Resource[] getResources(String directoryPath) {
        return Arrays.stream(getFiles(directoryPath))
            .map(File::getName)
            .map(fileName -> getResource(directoryPath + "/" + fileName))
            .toArray(Resource[]::new);
    }

    final void seedCards() throws IOException {

        int value = 1;
        for (Resource resource: getResources(DIRECTORY_NAME_CARDS)) {

            JsonNode jsonNode = objectMapper.readTree(resource.getFile());
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
        Resource resource = getResource("answers.json");
        AnswerDto[] answers = objectMapper.readValue(resource.getFile(), AnswerDto[].class);
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
        File[] files = getFiles(DIRECTORY_NAME_QUIZZES);


        for (File file: files) {
            // unmarshal from json
            QuizDto quizDto = objectMapper.readValue(file, QuizDto.class);

            // exercise
            ExerciseDto exerciseDto = objectMapper.readValue(objectMapper.readTree(file).traverse(), ExerciseDto.class);
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

    public final void seedMaps () throws IOException {
        for (File file: getFiles(DIRECTORY_NAME_MAPS)) {

            JsonNode jsonNode = objectMapper.readTree(file);

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