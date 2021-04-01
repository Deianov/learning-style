package bg.geist.init;

import bg.geist.constant.Constants;
import bg.geist.constant.ConstantsInit;
import bg.geist.domain.entity.*;
import bg.geist.domain.entity.enums.*;
import bg.geist.init.adapter.ArrayToMatrixAdapter;
import bg.geist.init.dto.*;
import bg.geist.repository.*;
import bg.geist.util.Flags;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static bg.geist.constant.Constants.*;
import static bg.geist.constant.ConstantsInit.*;

@Component
public class SeedDb {
    private final Resource jsonFile;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswersCollectionRepository answersCollectionRepository;
    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;
    private final CardsRepository cardsRepository;
    private final OptionsRepository optionsRepository;
    private final DictionaryCollectionRepository dictionaryCollectionRepository;
    private final DictionaryRepository dictionaryRepository;


    public SeedDb(@Value("classpath:myapp.json") Resource jsonFile, ObjectMapper objectMapper, UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, QuizRepository quizRepository, QuestionRepository questionRepository, AnswersCollectionRepository answersCollectionRepository, AnswerRepository answerRepository, CategoryRepository categoryRepository, CardsRepository cardsRepository, OptionsRepository optionsRepository, DictionaryCollectionRepository dictionaryCollectionRepository, DictionaryRepository dictionaryRepository) {
        this.jsonFile = jsonFile;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answersCollectionRepository = answersCollectionRepository;
        this.answerRepository = answerRepository;
        this.categoryRepository = categoryRepository;
        this.cardsRepository = cardsRepository;
        this.optionsRepository = optionsRepository;
        this.dictionaryCollectionRepository = dictionaryCollectionRepository;
        this.dictionaryRepository = dictionaryRepository;
    }

    public void showVersion() throws IOException {
        System.out.println(jsonFile.getFile().toString());
        HashMap<String, String> options = objectMapper
                .readValue(jsonFile.getFile(), new TypeReference<HashMap<String, String>>() {});
        System.out.printf("version: %s%n", options.get("version"));
    }

    public void seedUsers() {
        UserRoleEntity adminRole = new UserRoleEntity().setRole(UserRole.ADMIN);
        UserRoleEntity userRole = new UserRoleEntity().setRole(UserRole.USER);
        userRoleRepository.saveAll(List.of(adminRole, userRole));

        UserEntity admin = new UserEntity(ADMIN_NAME, passwordEncoder.encode(ADMIN_PASSWORD), ADMIN_FULLNAME, ADMIN_EMAIL);
        admin.setRoles(List.of(adminRole, userRole));

        UserEntity user = new UserEntity(USER_NAME, passwordEncoder.encode(USER_PASSWORD), USER_FULLNAME, USER_EMAIL);
        user.setRoles(List.of(userRole));

        userRepository.saveAll(List.of(admin, user));
    }

    public String currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public void seedCategories() {
        // parent
        categoryRepository.save(new Category("Cards"));
        categoryRepository.save(new Category("Quiz"));
        for (int i = 0; i < 3; i++) { categoryRepository.save(new Category("-")); }
        // cards
        categoryRepository.save(new Category("German", CARDS_BASE_CATEGORY_ID, 1));
        categoryRepository.save(new Category("Custom", CARDS_BASE_CATEGORY_ID, 2));
        // quizzes
        categoryRepository.save(new Category("JavaScript Advanced", QUIZ_BASE_CATEGORY_ID, 1));
        categoryRepository.save(new Category("Vue", QUIZ_BASE_CATEGORY_ID, 2));
        categoryRepository.save(new Category("GIS", QUIZ_BASE_CATEGORY_ID, 3));
    }

    public void seedCards() throws IOException {
        for (String fileName: new String[]{"1", "2", "3", "test"}) {

            Resource resource = new ClassPathResource(String.format("json/cards/%s.json", fileName));
            JsonNode jsonNode = objectMapper.readTree(resource.getFile());
            CardsDto cardsDto = objectMapper.readValue(jsonNode.traverse(), CardsDto.class);

            String[][] data;
            if(fileName.equals("3")) {
                String[] arr = objectMapper.treeToValue(jsonNode.get("dictionaries"), String[].class);
                data = ArrayToMatrixAdapter.toMatrix(arr, 2);
            } else {
                data = objectMapper.treeToValue(jsonNode.get("dictionaries"), String[][].class);
            }

            switch (fileName) {
                case "1":   // Die 200 wichtigsten deutschen Adjektive
                    seedCard(cardsDto, data, 1,
                            Lang.DE, Lang.DE, TextType.ADJECTIVE, TextRole.OPPOSITE, TextRole.TRANSLATION);
                    break;
                case "2":   // 34 special characters
                    seedCard(cardsDto, data, 2,
                            Lang.EN, Lang.DE, TextType.NOUN, TextRole.TRANSLATION, TextRole.CODE);
                    break;
                case "3":   // Diana's top 200
                    seedCard(cardsDto, data, 3,
                            Lang.BG, Lang.DE, TextType.SENTENCE, TextRole.TRANSLATION, null);
                    break;
                case "test": // Test
                    seedCard(cardsDto, data, 1,
                            Lang.EN, Lang.DE, TextType.NOUN, TextRole.TRANSLATION, TextRole.CODE);
            }
        }
    }

    public void seedCard(CardsDto cardsDto, String[][] data, int value,
                         Lang lang1, Lang lang2, TextType textType, TextRole textRole, TextRole attRole) {

        Cards cards = new Cards(cardsDto.name, cardsDto.description, value);
        cards.setSource(cardsDto.source)
                .setSourceUrl(cardsDto.sourceUrl).setAuthor(cardsDto.author).setAuthorUrl(cardsDto.authorUrl)
                .setCategoryId(categoryRepository.findByName(cardsDto.category).getId())
                .setCreatedBy(ADMIN_NAME);

        Options labels = new Options();
        Arrays.stream(cardsDto.labels).forEach(labels::add);
        cards.setLabels(optionsRepository.save(labels));

        Options options = new Options().fromMap(cardsDto.options);
        cards.setOptions(optionsRepository.save(options));

        DictionaryCollection dictionaryCollection = dictionaryCollectionRepository
                .save(new DictionaryCollection(null, data.length));
        cards.setDictionaryCollection(dictionaryCollection);

        Dictionary dictionary;
        int c = 1;
        for (String[] row : data) {
            dictionary = new Dictionary();
            dictionary.setValue(c++)
                    .setAtt1(row[0])
                    .setAtt2(row.length > 1 ? row[1] : null)
                    .setAtt3(row.length > 2 ? row[2] : null)
                    .setLang1(lang1)
                    .setLang2(lang2)
                    .setType(textType)
                    .setRole2(textRole)
                    .setRole3(attRole)
                    .setCollection(dictionaryCollection);
            dictionaryCollection.getDictionaries().add(dictionaryRepository.save(dictionary));
        }
        dictionaryCollectionRepository.save(dictionaryCollection);
        cardsRepository.save(cards);
        System.out.printf("flashcards: %s%n", cards.getName());
    }

    public void seedAnswersCollectionTemplates() {
        AnswersCollection answersCollectionBoolean = new AnswersCollection("BOOLEAN");
        answersCollectionBoolean.setValue(2);
        answersCollectionRepository.save(answersCollectionBoolean);
        answerRepository.save(new Answer("True",1, answersCollectionBoolean));
        answerRepository.save(new Answer("False",2, answersCollectionBoolean));
    }

    public void seedQuiz() throws IOException {
        int quizValue = 0;
        String lastCategoryName = "";

        for (int i = 1; i < 12; i++) {
            // unmarshal from json
            Resource resource = new ClassPathResource(String.format("json/quiz/%s.json", i));
            QuizDto quizDto = objectMapper.readValue(resource.getFile(), QuizDto.class);

            // category
            String categoryName = quizDto.category;
            long categoryId = categoryRepository.findByName(categoryName).getId();

            // sort value
            quizValue = categoryName.equals(lastCategoryName) ? quizValue + 1 : 1;
            lastCategoryName = categoryName;

            // quiz
            Quiz quiz = new Quiz(quizDto.name, quizDto.description, quizValue);
            quiz.setAuthor(quizDto.author);
            quiz.setAuthorUrl(quizDto.authorUrl);
            quiz.setSource(quizDto.source);
            quiz.setSourceUrl(quizDto.sourceUrl);
            quiz.setCategoryId(categoryId);
            quiz.setCreatedBy(ADMIN_NAME);

            // certification (Certification.NONE, ON_SERVER; ON_SERVER_STRICT)
            if (quizDto.options != null && quizDto.options.containsKey(QUIZ_CERTIFICATION_KEY)) {
                quiz.setCertification(Certification.byOrdinal(quizDto.options.get(QUIZ_CERTIFICATION_KEY)));
                quizDto.options.remove(QUIZ_CERTIFICATION_KEY);
            } else {
                quiz.setCertification(QUIZ_CERTIFICATION);
            }

            // options
            if (quizDto.options != null && quizDto.options.size() > 0) {
                Options options = new Options().fromMap(quizDto.options);
                quiz.setOptions(optionsRepository.save(options));
            }

            // questions
            Level level = categoryName.equals("GIS") ? Level.BASIC : Level.ADVANCED;
            Lang lang = categoryName.equals("JavaScript Advanced") ? Lang.BG : Lang.EN;
            Integer[] correct = quizDto.correct.toArray(Integer[]::new);
            int count = 0;
            for (QuestionDto questionDto : quizDto.questions) {
                int flags = correct[count];
                Question question = new Question(questionDto.text, count++, level, lang);

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
            System.out.printf("quiz: %s%n", quiz.getName());
        }
    }

    public void createAnswers (Question question, Collection<AnswerDto> answerDtos) {
        String[] answers = answerDtos.stream().map(answerDto -> answerDto.text).toArray(String[]::new);

        if (question.getType() == Question.Type.BOOLEAN) {
            question.setAnswersCollection(answersCollectionRepository.getOne(1L));
        } else {
            AnswersCollection answersCollection = new AnswersCollection();
            answersCollection.setValue(answers.length);
            answersCollectionRepository.save(answersCollection);
            question.setAnswersCollection(answersCollection);

            for (int i = 0; i < answers.length; i++) {
                answerRepository.save(new Answer(answers[i], Flags.toBits(i), answersCollection));
            }
        }
    }
}