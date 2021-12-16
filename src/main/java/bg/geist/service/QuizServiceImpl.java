package bg.geist.service;

import bg.geist.domain.entity.*;
import bg.geist.domain.entity.enums.ExerciseType;
import bg.geist.domain.model.service.QuizModelInt;
import bg.geist.exception.EntityName;
import bg.geist.constant.enums.ModelType;
import bg.geist.domain.entity.enums.Certification;
import bg.geist.domain.model.service.QuizModel;
import bg.geist.domain.model.service.QuizSimpleModel;
import bg.geist.exception.ObjectNotFoundException;
import bg.geist.repository.QuizRepository;
import bg.geist.web.api.model.ExerciseIndexModel;
import bg.geist.web.api.quizzes.models.QuizCertificationRequestModel;
import bg.geist.web.api.quizzes.models.QuizCertificationResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

import java.util.*;

import static bg.geist.constant.Constants.*;

@Service
public class QuizServiceImpl implements QuizService {
    private static final ExerciseType EXERCISE_TYPE = ExerciseType.QUIZZES;
    private static final EntityName ENTITY_NAME = EntityName.QUIZ;


    private final ModelMapper mapper;
    private final QuizRepository repository;
    private final ExerciseService exerciseService;
    private final CategoryService categoryService;

    public QuizServiceImpl(ModelMapper mapper, QuizRepository repository, ExerciseService exerciseService, CategoryService categoryService) {
        this.mapper = mapper;
        this.exerciseService = exerciseService;
        this.categoryService = categoryService;
        this.mapper.addMappings(typeMap1);
        this.mapper.addMappings(typeMap2);
        this.repository = repository;
    }

    // skip(destination.getCorrect());  don't work
    PropertyMap<Quiz, QuizModel> typeMap1 = new PropertyMap<>() {
        @Override
        protected void configure() {
            skip().setCorrect(null);
        }
    };
    PropertyMap<Quiz, QuizSimpleModel> typeMap2 = new PropertyMap<>() {
        @Override
        protected void configure() {
            skip().setCorrect(null);
        }
    };


    @Override
    public Collection<ExerciseIndexModel> getIndex() {
        Collection<ExerciseIndexModel> result = new ArrayList<>();

        for (Category category : categoryService.getCategories(EXERCISE_TYPE)) {
            ExerciseIndexModel exerciseCategory = new ExerciseIndexModel(category.getName());
            repository
                .findAllByCategory(category)
                .forEach(type -> exerciseCategory.addLink(type.getId(), type.getExercise().getName()));
            // skip empty categories
            if (!exerciseCategory.getLinks().isEmpty()) {
                result.add(exerciseCategory);
            }
        }
        return result;
    }

    @Override
    public Collection<Integer> getCorrect(Long id) {
        return repository.getCorrectAnswers(id);
    }


    @Override
    public <T extends QuizModelInt> T getModel(Long id, Class<T> tClass) {
        return map(getBy(id), tClass);
    }

    @Override
    public Object getResponseModel(Long id) {
        Quiz quiz = getBy(id);
        if (quiz.getCertification() == Certification.NONE &&
                QUIZZES_CERTIFICATION == Certification.NONE &&
                QUIZ_RESPONSE_MODEL == ModelType.MODEL) {
            return map(quiz, QuizModel.class);
        } else {
            return map(quiz, QuizSimpleModel.class);
        }
    }

    private <T extends QuizModelInt> T map(Quiz quiz, Class<T> tClass) {
        T quizModel = mapper.map(quiz, tClass);

        // skips correct answers when validation is on the server (ExerciseValidation.ON_SERVER)
        Certification certification = quiz.getCertification();
        if (certification == Certification.NONE) {
            quizModel.setCorrect(quiz.getCorrect());
        }
        // exercise
        quizModel.setExercise(exerciseService.map(quiz.getExercise(), quiz));
        // props
        HashMap<String, String> props = Prop.toMap(quiz.getProps());
        props.put("lang", quiz.getLang().name());
        props.put("level", quiz.getLevel().name());
        if (certification != Certification.NONE) {
            props.putIfAbsent(QUIZZES_CERTIFICATION_KEY, String.valueOf(certification.ordinal()));
        }
        quizModel.setProps(props);
        return quizModel;
    }

    @Override
    public QuizCertificationResponseModel certificate(QuizCertificationRequestModel requestModel) {
        Quiz quiz = getBy(requestModel.getQuizId());
        QuizCertificationResponseModel responseModel = new QuizCertificationResponseModel();
        Collection<Integer> answers = requestModel.getAnswers();
        Collection<Integer> correct = quiz.getCorrect();
        responseModel.setCorrect(correct);
        int right = 0;
        if (answers.size() != correct.size()) {
            throw new IllegalArgumentException("Bad request.");
        }
        Iterator<Integer> answersIterator = answers.iterator();
        for (Integer integer : correct) {
            if (integer.compareTo(answersIterator.next()) == 0) {
                right++;
            }
        }
        responseModel.setRight(right).setWrong(correct.size() - right);
        return responseModel;
    }


    private Quiz getBy(Long id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(ENTITY_NAME, id));
    }
}