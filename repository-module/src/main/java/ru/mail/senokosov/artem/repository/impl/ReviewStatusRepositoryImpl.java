package ru.mail.senokosov.artem.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.ReviewStatusRepository;
import ru.mail.senokosov.artem.repository.model.ReviewStatus;

@Repository
public class ReviewStatusRepositoryImpl extends GenericRepositoryImpl<Long, ReviewStatus> implements ReviewStatusRepository {
}
