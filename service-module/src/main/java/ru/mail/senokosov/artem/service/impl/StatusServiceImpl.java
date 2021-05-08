package ru.mail.senokosov.artem.service.impl;

import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.StatusRepository;
import ru.mail.senokosov.artem.repository.model.Status;
import ru.mail.senokosov.artem.repository.model.enums.StatusEnum;
import ru.mail.senokosov.artem.service.StatusService;
import ru.mail.senokosov.artem.service.converter.StatusConverter;

import javax.transaction.Transactional;

@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;
    private final StatusConverter statusConverter;

    public StatusServiceImpl(StatusRepository statusRepository, StatusConverter statusConverter) {
        this.statusRepository = statusRepository;
        this.statusConverter = statusConverter;
    }

    @Override
    @Transactional
    public void addStatus(StatusEnum statusEnum) {
        Status status = statusConverter.convert(statusEnum);
        statusRepository.persist(status);
    }
}
