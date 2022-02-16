package bg.geist.domain.entity.enums;

public enum ExerciseType {
    CARDS("Cards"),
    QUIZZES("Quizzes"),
    MAPS("Maps");

    public final String value;

    ExerciseType(String value) {
        this.value = value;
    }
}