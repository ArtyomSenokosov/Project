package ru.mail.senokosov.artem.service.converter.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Comment;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.converter.CommentConverter;
import ru.mail.senokosov.artem.service.model.CommentDTO;

import java.time.LocalDateTime;
import java.util.Objects;

import static ru.mail.senokosov.artem.service.util.ServiceUtil.getFormatDateTime;

@Component
@RequiredArgsConstructor
public class CommentConverterImpl implements CommentConverter {

    private final ModelMapper modelMapper;

    @Override
    public CommentDTO convert(Comment comment) {
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);

        LocalDateTime date = comment.getDateOfCreation();
        if (Objects.nonNull(date)) {
            String dateTime = getFormatDateTime(date);
            commentDTO.setDateOfCreation(dateTime);
        }

        String fullContent = comment.getContent();
        commentDTO.setFullContent(fullContent);

        User user = comment.getUser();
        if (Objects.nonNull(user)) {
            String lastName = user.getLastName();
            commentDTO.setLastName(lastName);
            String firstName = user.getFirstName();
            commentDTO.setFirstName(firstName);
        }

        return commentDTO;
    }

    @Override
    public Comment convert(CommentDTO commentDTO) {
        Comment comment = new Comment();

        LocalDateTime localDateTime = LocalDateTime.now();
        comment.setDateOfCreation(localDateTime);

        String fullContent = commentDTO.getFullContent();
        comment.setContent(fullContent);

        return comment;
    }
}