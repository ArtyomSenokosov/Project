package ru.mail.senokosov.artem.service.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.mail.senokosov.artem.service.model.PageDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static java.lang.Math.*;

@Log4j2
@RequiredArgsConstructor
public class ServiceUtil {

    public static String getFormatDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");//константа
        return localDateTime.format(formatter);
    }
}
