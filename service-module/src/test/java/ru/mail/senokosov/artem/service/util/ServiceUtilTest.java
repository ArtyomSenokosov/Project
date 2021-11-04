package ru.mail.senokosov.artem.service.util;

import org.junit.jupiter.api.Test;
import ru.mail.senokosov.artem.service.constant.PasswordGenerateConstant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceUtilTest {

    @Test
    void shouldReturnFormattedDateTimeWhenFormatDateTimeIsCalled() {
        LocalDateTime testDateTime = LocalDateTime.of(2023, 3, 21, 13, 45);
        String expectedFormat = testDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String actualFormat = ServiceUtil.getFormatDateTime(testDateTime);

        assertEquals(expectedFormat, actualFormat, "The formatted date-time string does not match the expected format.");
    }

    @Test
    void shouldGenerateRandomPasswordWithCorrectLength() {
        String password = ServiceUtil.generateRandomPassword();
        assertEquals(PasswordGenerateConstant.NUMBER_OF_CHARS_IN_PASSWORD, password.length(), "Generated password length does not match the expected.");
    }

    @Test
    void shouldEnsureGeneratedRandomPasswordContainsOnlySpecifiedCharacters() {
        String password = ServiceUtil.generateRandomPassword();
        for (char character : password.toCharArray()) {
            assertTrue(PasswordGenerateConstant.ALPHA_NUMERIC_STRING.contains(String.valueOf(character)),
                    "Generated password contains characters outside the specified set.");
        }
    }
}