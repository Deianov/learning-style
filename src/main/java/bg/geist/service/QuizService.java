package bg.geist.service;


import bg.geist.domain.model.service.QuizModelInt;
import bg.geist.web.api.model.ExerciseIndexModel;
import bg.geist.web.api.quizzes.models.QuizCertificationRequestModel;
import bg.geist.web.api.quizzes.models.QuizCertificationResponseModel;

import java.util.Collection;

public interface QuizService {
    Collection<ExerciseIndexModel> getIndex();
    Collection<Integer> getCorrect(Long id);

    <T extends QuizModelInt> T getModel(Long id, Class<T> tClass);
    Object getResponseModel(Long id);

    QuizCertificationResponseModel certificate(QuizCertificationRequestModel requestModel);
}
