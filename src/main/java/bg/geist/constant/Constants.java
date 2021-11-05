package bg.geist.constant;

import bg.geist.domain.entity.enums.Certification;
import bg.geist.constant.enums.ModelType;

public final class Constants {

    // USERS
    public static final String USER_WITH_NAME_NOT_FOUND = "User with name '%s' was not found!";
    public static final String USER_WITH_EMAIL_NOT_FOUND = "User with email '%s' was not found!";
    public static final String USER_WITH_PROFILE_NOT_FOUND = "User with profile '%d' was not found!";
    public static final String USER_ROLE_NOT_FOUND = "USER role not found. Please seed the roles.";
    public static final String USER_ROLE_FOUND = "User role found.";
    public static final String USER_BAD_CREDENTIALS = "User bad credentials.";

    public static final int USERNAME_MIN_LENGTH = 3;
    public static final String USERNAME_EMPTY_MESSAGE = "Username cannot be empty.";
    public static final String USERNAME_EXISTS_MESSAGE = "Username is already occupied.";

    public static final int FULLNAME_MIN_LENGTH = 3;
    public static final String FULLNAME_EMPTY_MESSAGE = "Full name cannot be empty.";
    public static final String FULLNAME_LENGTH_MESSAGE = "Full name length must be more than 3 characters.";

    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 20;
    public static final String NEW_PASSWORDS_REGEX = "^(|[\\w\\W\\d\\s]{4,20})$";
    public static final String PASSWORDS_REGEX = "^[\\w\\W\\d\\s]{4,20}$";

    public static final String PASSWORD_EMPTY_MESSAGE = "Password cannot be empty.";
    public static final String PASSWORDS_MATCH_MESSAGE = "Password and Confirm Password does not match.";
    public static final String PASSWORDS_LENGTH_MESSAGE = "Password length must be between {4} and {20} characters.";
    public static final String PASSWORDS_CHECK_MESSAGE = "Password is not valid.";

    public static final String EMAIL_EMPTY_MESSAGE = "Email cannot be empty.";
    public static final String EMAIL_REGEX = "\\b[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,63}\\b";
    public static final String EMAIL_MESSAGE = "Enter valid email address.";
    public static final String EMAIL_EXISTS_MASSAGE = "This email address is already being used.";

    // USER PROFILE
    public static final String PROFILE_DEFAULT_AVATAR = "/assets/images/avatar.png";
    public static final String PROFILE_KEY = "profile";


    // CATEGORIES
    public static final long CATEGORY_ID_CARDS = 1L;
    public static final long CATEGORY_ID_QUIZZES = 2L;
    public static final long CATEGORY_ID_MAPS = 3L;

    // QUIZ
    public static final Certification QUIZZES_CERTIFICATION = Certification.NONE;
    public static final String QUIZZES_CERTIFICATION_KEY = "certification";
    public static final ModelType QUIZ_RESPONSE_MODEL = ModelType.SIMPLE;

    public static final class PRJ {
        public static final String RESOURCES_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\json\\";
        public static final String RESOURCES_PATH_INPUT = RESOURCES_PATH + "input";
        public static final String RESOURCES_PATH_OUTPUT = RESOURCES_PATH + "output";

        public static final String DIRECTORY_NAME_CARDS = "cards";
        public static final String DIRECTORY_NAME_QUIZZES = "quizzes";
        public static final String DIRECTORY_NAME_MAPS = "maps";
    }

    public static final class INIT {
        public static final String ADMIN_NAME = "admin";
        public static final String ADMIN_PASSWORD = "admin";
        public static final String ADMIN_FULLNAME = "TEST ADMINISTRATOR";
        public static final String ADMIN_EMAIL = "admin@admin.com";

        public static final String USER_NAME = "user";
        public static final String USER_PASSWORD = "user";
        public static final String USER_FULLNAME = "TEST USER";
        public static final String USER_EMAIL = "user@user.com";
        public static final String USER_IMAGE = "https://res.cloudinary.com/deianov/image/upload/v1617366058/user.jpg";
    }
}