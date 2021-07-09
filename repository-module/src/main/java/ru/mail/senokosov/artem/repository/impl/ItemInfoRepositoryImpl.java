package ru.mail.senokosov.artem.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.senokosov.artem.repository.ItemInfoRepository;
import ru.mail.senokosov.artem.repository.model.ItemInfo;

@Repository
public class ItemInfoRepositoryImpl extends GenericRepositoryImpl<Long, ItemInfo> implements ItemInfoRepository {
}