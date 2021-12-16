package bg.geist.domain.model.service;

import java.util.HashMap;

public class ExerciseParentModel implements ExerciseParentModelInt {
    private ExerciseModel exercise;
    private HashMap<String, String> props;

    public ExerciseParentModel() { }

    public ExerciseModel getExercise() {
        return exercise;
    }
    public void setExercise(ExerciseModel exercise) {
        this.exercise = exercise;
    }
    public HashMap<String, String> getProps() {
        return props;
    }
    public void setProps(HashMap<String, String> props) {
        this.props = props;
    }
}
