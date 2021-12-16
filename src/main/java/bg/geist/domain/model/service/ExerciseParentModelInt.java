package bg.geist.domain.model.service;

import java.util.HashMap;

public interface ExerciseParentModelInt {
    ExerciseModel getExercise();
    HashMap<String, String> getProps();
    void setExercise(ExerciseModel exercise);
    void setProps(HashMap<String, String> props);
}