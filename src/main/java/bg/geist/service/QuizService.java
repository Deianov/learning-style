package bg.geist.service;


import bg.geist.domain.model.ExerciseModel;
import bg.geist.web.api.exercise.ExerciseIndexModel;
import bg.geist.web.api.quiz.models.QuizValidationRequestModel;
import bg.geist.web.api.quiz.models.QuizValidationResponseModel;

import java.util.Collection;

public interface QuizService {
    Collection<ExerciseIndexModel> getIndex();
    Collection<Integer> getCorrect(Long id);

    <T extends ExerciseModel> T getModel(Long id, Class<T> tClass);
    Object getResponseModel(Long id);

    QuizValidationResponseModel validate(QuizValidationRequestModel requestModel);
}
