package ru.mail.senokosov.artem.web.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.senokosov.artem.service.CommentService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.add.AddCommentDTO;

import javax.validation.Valid;

import static ru.mail.senokosov.artem.web.constant.PathConstant.*;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = CUSTOMER_PATH + COMMENTS_PATH + "/add")
    public String addPage(Model model) {
        model.addAttribute("addCommentDTO", new AddCommentDTO());
        return "add-comment";
    }

    @PostMapping(value = CUSTOMER_PATH + COMMENTS_PATH + "/add")
    public String add(@Valid AddCommentDTO addCommentDTO, BindingResult result,
                      @RequestParam(name = "articleId") Long id) throws ServiceException {
        if (result.hasErrors()) {
            return "add-comment";
        } else {
            commentService.persist(addCommentDTO, id);
        }
        return String.format("redirect:/customer/articles/%d", id);
    }

    @GetMapping(value = SELLER_PATH + COMMENTS_PATH + "/{id}/delete")
    public String deleteArticle(@PathVariable("id") Long id) {
        commentService.isDeleteById(id);
        return String.format("redirect:/seller/articles/%d", id);
    }
}