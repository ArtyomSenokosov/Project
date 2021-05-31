package ru.mail.senokosov.artem.repository;

import ru.mail.senokosov.artem.repository.model.Article;
import ru.mail.senokosov.artem.repository.model.Item;

import java.util.List;

public interface ItemRepository extends GenericRepository<Long, Item> {

    Long getCountArticle();

    List<Item> findAll(int pageNumber, int pageSize);
}
