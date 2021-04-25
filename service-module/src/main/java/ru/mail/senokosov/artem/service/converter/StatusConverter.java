package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Status;
import ru.mail.senokosov.artem.repository.model.enums.StatusEnum;

public interface StatusConverter {

    Status convert(StatusEnum statusEnum);
}
