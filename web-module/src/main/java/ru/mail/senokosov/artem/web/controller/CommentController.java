package ru.mail.senokosov.artem.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.senokosov.artem.service.CommentService;
import ru.mail.senokosov.artem.service.model.add.AddCommentDTO;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/customer/comments/add")
    public String addPage(Model model) {
        model.addAttribute("comment", new AddCommentDTO());
        return "add-comment";
    }

    @PostMapping(value = "/customer/comments/add")
    public String add(@Valid AddCommentDTO addCommentDTO, BindingResult error,
                      @RequestParam(value = "username") String userName,
                      @RequestParam(name = "article") Long id) throws SecurityException {
        if (error.hasErrors()) {
            log.info("errors:{}", error);
            return "add-comment";
        } else {
            commentService.persist(addCommentDTO, userName, id);
            log.info("go persist comment");
        }
        return "redirect:/customer/articles";
    }
}
