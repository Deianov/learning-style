package bg.geist.service;

import bg.geist.domain.entity.Category;
import bg.geist.domain.entity.Options;
import bg.geist.domain.entity.Quiz;
import bg.geist.domain.enums.EntityName;
import bg.geist.domain.enums.ExerciseValidation;
import bg.geist.domain.enums.ModelType;
import bg.geist.domain.model.ExerciseModel;
import bg.geist.domain.model.QuizModel;
import bg.geist.domain.model.QuizSimpleModel;
import bg.geist.exception.EntityNotFoundException;
import bg.geist.repository.CategoryRepository;
import bg.geist.repository.QuizRepository;
import bg.geist.web.api.exercise.ExerciseIndexModel;
import bg.geist.web.api.quiz.models.QuizValidationRequestModel;
import bg.geist.web.api.quiz.models.QuizValidationResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

import java.util.*;

import static bg.geist.constant.Constants.*;

@Service
public class QuizServiceImpl implements QuizService {

    private final ModelMapper mapper;
    private final QuizRepository repository;
    private final CategoryRepository categoryRepository;

    public QuizServiceImpl(ModelMapper mapper, QuizRepository repository, CategoryRepository categoryRepository) {
        this.mapper = mapper;
        this.mapper.addMappings(typeMap1);
        this.mapper.addMappings(typeMap2);
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    PropertyMap<Quiz, QuizModel> typeMap1 = new PropertyMap<>() {
        @Override
        protected void configure() {
//            skip(destination.getCorrect());  don't work
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
        for (Category category : categoryRepository.findAllByParentId(QUIZ_BASE_CATEGORY_ID)) {
            ExerciseIndexModel exerciseCategory = new ExerciseIndexModel(category.getName());
            repository.findAllByCategoryId(category.getId())
                    .forEach(quiz -> exerciseCategory.addLink(quiz.getId(), quiz.getName()));
            // skip empty categories
            if (!exerciseCategory.getLinks().isEmpty()) {
                result.add(exerciseCategory);
            }
        }

        return result;
    }

    @Override
    public Collection<Integer> getCorrect(Long id) {
        return repository.getCorrectAnswers(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityName.QUIZ, id));
    }

    private Quiz getBy(Long id) throws EntityNotFoundException {
        return repository.fetchById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityName.QUIZ, id));
    }

    @Override
    public <T extends ExerciseModel> T getModel(Long id, Class<T> tClass) {
        return map(getBy(id), tClass);
    }

    @Override
    public Object getResponseModel(Long id) {
        Quiz quiz = getBy(id);
        if (quiz.getValidation() == ExerciseValidation.NONE &&
                QUIZ_DEFAULT_VALIDATION == ExerciseValidation.NONE &&
                QUIZ_DEFAULT_MODEL == ModelType.MODEL) {
            return map(quiz, QuizModel.class);
        } else {
            return map(quiz, QuizSimpleModel.class);
        }
    }

    private <T extends ExerciseModel> T map(Quiz quiz, Class<T> tClass) {
        T quizModel = mapper.map(quiz, tClass);

        // skips correct answers when validation is on the server (ExerciseValidation.ON_SERVER)
        ExerciseValidation validation = quiz.getValidation();
        if (validation == ExerciseValidation.NONE) {
            quizModel.setCorrect(quiz.getCorrect());
        }

        Options options = quiz.getOptions();
        if (options != null) {
            HashMap<String, Integer> opts = options.toMap();
            opts.putIfAbsent(QUIZ_KEY_VALIDATION, quiz.getValidation().ordinal());
            quizModel.setOptions(opts);
        }
        categoryRepository.findById(quiz.getCategoryId())
                .ifPresent(category -> quizModel.setCategory(category.getName()));
        return quizModel;
    }

    @Override
    public QuizValidationResponseModel validate(QuizValidationRequestModel requestModel) {
        Quiz quiz = getBy(requestModel.getQuizId());
        QuizValidationResponseModel responseModel = new QuizValidationResponseModel();
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
}