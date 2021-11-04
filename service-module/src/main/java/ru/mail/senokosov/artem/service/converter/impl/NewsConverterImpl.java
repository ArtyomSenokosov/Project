package ru.mail.senokosov.artem.service.converter.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.News;
import ru.mail.senokosov.artem.repository.model.Comment;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.converter.NewsConverter;
import ru.mail.senokosov.artem.service.converter.CommentConverter;
import ru.mail.senokosov.artem.service.model.NewsDTO;
import ru.mail.senokosov.artem.service.model.CommentDTO;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.mail.senokosov.artem.service.constant.NewsConstant.MAXIMUM_CHARS_FOR_SHORT_CONTENT_FIELD;
import static ru.mail.senokosov.artem.service.util.ServiceUtil.getFormatDateTime;

@Component
@RequiredArgsConstructor
public class NewsConverterImpl implements NewsConverter {

    private final CommentConverter commentConverter;
    private final ModelMapper modelMapper;

    @Override
    public NewsDTO convert(News news) {
        NewsDTO newsDTO = modelMapper.map(news, NewsDTO.class);

        LocalDateTime lastUpdateDate = news.getLastDateUpdate();
        LocalDateTime creationDate = news.getDateOfCreation();

        LocalDateTime dateToShow = Objects.nonNull(lastUpdateDate) ? lastUpdateDate : creationDate;
        String dateTime = getFormatDateTime(dateToShow);
        newsDTO.setDateOfCreation(dateTime);

        User user = news.getUser();
        if (Objects.nonNull(user)) {
            String firstName = user.getFirstName();
            newsDTO.setFirstName(firstName);
            String lastName = user.getLastName();
            newsDTO.setLastName(lastName);
        }

        String fullContent = news.getContent();
        newsDTO.setFullContent(fullContent);
        addShortContent(newsDTO, fullContent);

        Set<Comment> comments = news.getComments();
        if (!comments.isEmpty()) {
            List<CommentDTO> commentDTOs = comments.stream()
                    .sorted(Comparator.comparing(Comment::getDateOfCreation).reversed())
                    .map(commentConverter::convert)
                    .collect(Collectors.toList());
            newsDTO.getComments().clear();
            newsDTO.getComments().addAll(commentDTOs);
        }

        return newsDTO;
    }

    @Override
    public News convert(NewsDTO newsDTO) {
        News news = new News();

        String title = newsDTO.getTitle();
        news.setTitle(title);

        String content = newsDTO.getFullContent();
        news.setContent(content);

        return news;
    }

    private void addShortContent(NewsDTO showNewsDTO, String fullContent) {
        if (fullContent.length() > MAXIMUM_CHARS_FOR_SHORT_CONTENT_FIELD) {
            String shortContent = fullContent.substring(0, MAXIMUM_CHARS_FOR_SHORT_CONTENT_FIELD);
            showNewsDTO.setShortContent(shortContent);
        } else {
            showNewsDTO.setShortContent(fullContent);
        }
    }
}