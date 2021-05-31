package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Article;
import ru.mail.senokosov.artem.service.model.add.AddArticleDTO;
import ru.mail.senokosov.artem.service.model.ArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;

public interface ArticleConverter {

    ShowArticleDTO convert(Article article);

    Article convert(AddArticleDTO addArticleDTO);

    ArticleDTO convertToChange(Article article);
}
