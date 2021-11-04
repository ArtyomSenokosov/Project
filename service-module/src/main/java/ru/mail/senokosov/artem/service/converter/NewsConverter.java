package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.News;
import ru.mail.senokosov.artem.service.model.NewsDTO;

public interface NewsConverter {

    NewsDTO convert(News news);

    News convert(NewsDTO newsDTO);
}