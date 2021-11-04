package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.CommentRepository;
import ru.mail.senokosov.artem.service.CommentService;

import javax.transaction.Transactional;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public boolean isDeleteById(Long id) {
        try {
            if (Objects.nonNull(commentRepository.findById(id))) {
                commentRepository.removeById(id);
                log.info("Comment with id {} has been successfully deleted.", id);
                return true;
            } else {
                log.warn("Comment with id {} not found. Deletion cannot be performed.", id);
                return false;
            }
        } catch (Exception exception) {
            log.error("Error occurred during the deletion of comment with id {}: {}", id, exception.getMessage());
            return false;
        }
    }
}