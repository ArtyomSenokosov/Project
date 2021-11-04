package ru.mail.senokosov.artem.service.util;

import lombok.RequiredArgsConstructor;
import ru.mail.senokosov.artem.service.constant.PasswordGenerateConstant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static ru.mail.senokosov.artem.service.constant.FormatConstant.DATE_FORMAT_PATTERN;

@RequiredArgsConstructor
public final class ServiceUtil {

    public static String getFormatDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
        return localDateTime.format(formatter);
    }

    public static String generateRandomPassword() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < PasswordGenerateConstant.NUMBER_OF_CHARS_IN_PASSWORD; i++) {
            int character = (random.nextInt(PasswordGenerateConstant.ALPHA_NUMERIC_STRING.length()));
            builder.append(PasswordGenerateConstant.ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}