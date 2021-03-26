package bg.geist.constant;

import bg.geist.domain.enums.ExerciseValidation;
import bg.geist.domain.enums.ModelType;

public class Constants {
    public static String CURRENT_USER = "admin";

    // CARDS
    public static long CARDS_BASE_CATEGORY_ID = 1L;

    // QUIZ
    public static long QUIZ_BASE_CATEGORY_ID = 2L;
    public static ExerciseValidation QUIZ_DEFAULT_VALIDATION = ExerciseValidation.NONE;
    public static ModelType QUIZ_DEFAULT_MODEL = ModelType.SIMPLE;
    public static String QUIZ_KEY_VALIDATION = "validation";
}