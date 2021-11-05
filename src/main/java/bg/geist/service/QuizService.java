package bg.geist.service;


import bg.geist.domain.model.service.ExerciseModel;
import bg.geist.web.api.exercise.ExerciseIndexModel;
import bg.geist.web.api.quizzes.models.QuizCertificationRequestModel;
import bg.geist.web.api.quizzes.models.QuizCertificationResponseModel;

import java.util.Collection;

public interface QuizService {
    Collection<ExerciseIndexModel> getIndex();
    Collection<Integer> getCorrect(Long id);

    <T extends ExerciseModel> T getModel(Long id, Class<T> tClass);
    Object getResponseModel(Long id);

    QuizCertificationResponseModel certificate(QuizCertificationRequestModel requestModel);
}
