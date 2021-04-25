package ru.mail.senokosov.artem.service;

import ru.mail.senokosov.artem.repository.model.enums.StatusEnum;

public interface StatusService {

    void addStatus(StatusEnum statusEnum);
}
