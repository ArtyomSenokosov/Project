package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.Item;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends GenericRepository<Long, Item> {

    Item findByUuid(UUID uuid);

    List<Item> findAll(Integer startPosition, int maximumItemsOnPage);
}