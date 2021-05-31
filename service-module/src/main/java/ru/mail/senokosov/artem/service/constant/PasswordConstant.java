package ru.mail.senokosov.artem.service.constant;

public interface PasswordConstant {

    String CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    String CHAR_UPPERCASE = CHAR_LOWERCASE.toUpperCase();
    String DIGIT = "0123456789";
    String OTHER_PUNCTUATION = "!@#&()–[{}]:;',?/*";
    String OTHER_SYMBOL = "~$^+=<>";
    String OTHER_SPECIAL = OTHER_PUNCTUATION + OTHER_SYMBOL;
    int PASSWORD_LENGTH = 20;
    String PASSWORD_ALLOW =
            CHAR_LOWERCASE + CHAR_UPPERCASE + DIGIT + OTHER_SPECIAL;
}
