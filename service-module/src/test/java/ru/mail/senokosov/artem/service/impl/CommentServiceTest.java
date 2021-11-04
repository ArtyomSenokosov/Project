package ru.mail.senokosov.artem.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.mail.senokosov.artem.repository.CommentRepository;
import ru.mail.senokosov.artem.repository.model.Comment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void shouldDeleteCommentByIdWhenExists() {
        Long commentId = 1L;
        Comment mockComment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(mockComment);

        boolean result = commentService.isDeleteById(commentId);

        assertTrue(result);
        verify(commentRepository, times(1)).removeById(commentId);
    }

    @Test
    void shouldReturnFalseAndLogWarningWhenCommentDoesNotExist() {
        Long commentId = 2L;
        when(commentRepository.findById(commentId)).thenReturn(null);

        boolean result = commentService.isDeleteById(commentId);

        assertFalse(result);
        verify(commentRepository, never()).removeById(anyLong());
    }

    @Test
    void shouldReturnFalseAndLogErrorWhenDeletionFails() {
        Long commentId = 3L;
        when(commentRepository.findById(commentId)).thenReturn(new Comment());
        doThrow(new RuntimeException("Database error")).when(commentRepository).removeById(commentId);

        boolean result = commentService.isDeleteById(commentId);

        assertFalse(result);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).removeById(commentId);
    }
}
