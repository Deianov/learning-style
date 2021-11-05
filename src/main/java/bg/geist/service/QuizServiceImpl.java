package bg.geist.service;

import bg.geist.exception.EntityName;
import bg.geist.constant.enums.ModelType;
import bg.geist.domain.entity.Category;
import bg.geist.domain.entity.Options;
import bg.geist.domain.entity.Quiz;
import bg.geist.domain.entity.enums.Certification;
import bg.geist.domain.model.service.ExerciseModel;
import bg.geist.domain.model.service.QuizModel;
import bg.geist.domain.model.service.QuizSimpleModel;
import bg.geist.exception.ObjectNotFoundException;
import bg.geist.repository.CategoryRepository;
import bg.geist.repository.QuizRepository;
import bg.geist.web.api.exercise.ExerciseIndexModel;
import bg.geist.web.api.quizzes.models.QuizCertificationRequestModel;
import bg.geist.web.api.quizzes.models.QuizCertificationResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

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
        for (Category category : categoryRepository.findAllByParentId(CATEGORY_ID_QUIZZES)) {
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
        return repository.getCorrectAnswers(id);
    }

    private Quiz getBy(Long id) throws ObjectNotFoundException {
        return repository.fetchById(id)
                .orElseThrow(() -> new ObjectNotFoundException(EntityName.QUIZ, id));
    }

    @Override
    public <T extends ExerciseModel> T getModel(Long id, Class<T> tClass) {
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

    private <T extends ExerciseModel> T map(Quiz quiz, Class<T> tClass) {
        T quizModel = mapper.map(quiz, tClass);

        // skips correct answers when validation is on the server (ExerciseValidation.ON_SERVER)
        Certification validation = quiz.getCertification();
        if (validation == Certification.NONE) {
            quizModel.setCorrect(quiz.getCorrect());
        }

        Options options = quiz.getOptions();
        if (options != null) {
            HashMap<String, Integer> opts = options.toMap();
            opts.putIfAbsent(QUIZZES_CERTIFICATION_KEY, quiz.getCertification().ordinal());
            quizModel.setOptions(opts);
        }
        categoryRepository.findById(quiz.getCategoryId())
                .ifPresent(category -> quizModel.setCategory(category.getName()));
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
}