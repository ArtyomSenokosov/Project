package ru.mail.senokosov.artem.service.util;

import org.junit.jupiter.api.Test;
import ru.mail.senokosov.artem.service.model.dto.TestDto;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void shouldCorrectlyExtractSizeConstraintsWhenFieldSizeConstraintsAreRequested() {
        Map<String, Map<String, Integer>> constraints = ValidationUtil.getFieldSizeConstraints(TestDto.class);

        assertEquals(2, constraints.size(), "Constraints map should contain two entries.");

        assertTrue(constraints.containsKey("shortText"), "Constraints map should contain 'shortText'.");
        assertEquals(1, constraints.get("shortText").get("min").intValue(), "Min size for 'shortText' should be 1.");
        assertEquals(10, constraints.get("shortText").get("max").intValue(), "Max size for 'shortText' should be 10.");

        assertTrue(constraints.containsKey("longText"), "Constraints map should contain 'longText'.");
        assertEquals(5, constraints.get("longText").get("min").intValue(), "Min size for 'longText' should be 5.");
        assertEquals(255, constraints.get("longText").get("max").intValue(), "Max size for 'longText' should be 255.");
    }

    @Test
    void shouldIgnoreFieldsWithoutSizeAnnotationWhenFieldSizeConstraintsAreRequested() {
        Map<String, Map<String, Integer>> constraints = ValidationUtil.getFieldSizeConstraints(TestDto.class);

        assertFalse(constraints.containsKey("noSizeText"), "Constraints map should not contain 'noSizeText'.");
    }
}