package ru.mail.senokosov.artem.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.StatusRepository;
import ru.mail.senokosov.artem.repository.model.Status;

import java.util.List;

@Repository
public class StatusRepositoryImpl extends GenericRepositoryImpl<Long, Status> implements StatusRepository {

}
