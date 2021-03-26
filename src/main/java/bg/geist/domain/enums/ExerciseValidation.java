package bg.geist.domain.enums;

public enum ExerciseValidation {
    NONE,
    ON_SERVER,
    ON_SERVER_STRICT,
    GET_FROM_ENTITY;

    private static final ExerciseValidation[] values = values();
    public static ExerciseValidation byOrdinal(Integer ordinal) {
        return (ordinal != null && ordinal > -1 && ordinal < values.length) ? values[ordinal] : values[0];
    }
}