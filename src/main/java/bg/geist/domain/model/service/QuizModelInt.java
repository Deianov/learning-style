package bg.geist.domain.model.service;

import java.util.Collection;

public interface QuizModelInt extends ExerciseParentModelInt {
    Collection<Integer> getCorrect();
    void setCorrect(Collection<Integer> correct);
}