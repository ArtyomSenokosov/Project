package ru.mail.senokosov.artem.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Status;
import ru.mail.senokosov.artem.repository.model.enums.StatusEnum;
import ru.mail.senokosov.artem.service.converter.StatusConverter;

@Component
public class StatusConverterImpl implements StatusConverter {
    @Override
    public Status convert(StatusEnum statusEnum) {
        Status status = new Status();
        status.setStatus(statusEnum);
        return status;
    }
}
