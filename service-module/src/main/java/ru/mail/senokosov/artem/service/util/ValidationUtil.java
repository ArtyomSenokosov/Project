package ru.mail.senokosov.artem.service.util;

import java.lang.reflect.Field;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

public class ValidationUtil {

    public static Map<String, Map<String, Integer>> getFieldSizeConstraints(Class<?> dtoClass) {
        Map<String, Map<String, Integer>> constraints = new HashMap<>();
        Field[] fields = dtoClass.getDeclaredFields();

        for (Field field : fields) {
            Size size = field.getAnnotation(Size.class);
            if (size != null) {
                Map<String, Integer> sizes = new HashMap<>();
                sizes.put("min", size.min());
                sizes.put("max", size.max());
                constraints.put(field.getName(), sizes);
            }
        }
        return constraints;
    }
}
