package ru.mail.senokosov.artem.service.constant;

public interface UserValidationConstant {

    int MAXIMUM_USERS_ON_PAGE = 10;
    int MINIMUM_LAST_NAME_SIZE = 2;
    int MAXIMUM_LAST_NAME_SIZE = 40;
    int MINIMUM_FIRST_NAME_SIZE = 2;
    int MAXIMUM_FIRST_NAME_SIZE = 20;
    int MINIMUM_MIDDLE_NAME_SIZE = 2;
    int MAXIMUM_MIDDLE_NAME_SIZE = 40;
    int MINIMUM_EMAIL_NAME_SIZE = 2;
    int MAXIMUM_EMAIL_NAME_SIZE = 50;
    int MAXIMUM_ADDRESS_SIZE = 255;
    int MAXIMUM_TELEPHONE_SIZE = 20;
    String ONLY_LATIN_LETTERS_REGEXP = "^[+\\-()\\d\\s]{7,20}$";
    String EMAIL_REGEXP = "^[\\w.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
}