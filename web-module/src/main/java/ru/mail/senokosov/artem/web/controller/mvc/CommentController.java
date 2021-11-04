package ru.mail.senokosov.artem.web.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.mail.senokosov.artem.service.CommentService;

import static ru.mail.senokosov.artem.web.constant.PathConstant.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping(value = {SELLER_PATH + COMMENTS_PATH + "/{id}/delete",
            ADMIN_PATH + COMMENTS_PATH + "/{id}/delete"})
    public String deleteArticle(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRATOR"))) {
            log.info("Admin with username '{}' is deleting comment with id {}", authentication.getName(), id);
            commentService.isDeleteById(id);
            return "redirect:" + ADMIN_PATH + NEWS_PATH + "/" + id;
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("SALE_USER"))) {
            log.info("Seller with username '{}' is deleting comment with id {}", authentication.getName(), id);
            commentService.isDeleteById(id);
            return "redirect:" + SELLER_PATH + NEWS_PATH + "/" + id;
        } else {
            log.warn("Unauthorized attempt to delete comment with id {} by username '{}'", id, authentication.getName());
            return "redirect:" + ACCESS_DENIED_PATH;
        }
    }
}