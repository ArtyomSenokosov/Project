package ru.mail.senokosov.artem.web.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mail.senokosov.artem.service.ArticleService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.change.ChangeArticleDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.add.AddArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;

import javax.validation.Valid;

import static ru.mail.senokosov.artem.web.constant.PathConstant.*;

@RequiredArgsConstructor
@Log4j2
@Controller
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping(value = {CUSTOMER_PATH + ARTICLES_PATH,
            SELLER_PATH + ARTICLES_PATH,
            ADMIN_PATH + ARTICLES_PATH})
    public String getArticlesByPagination(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        PageDTO pageDTO = articleService.getArticlesByPage(page);
        model.addAttribute("pageDTO", pageDTO);
        return "articles";
    }

    @GetMapping(value = {CUSTOMER_PATH + ARTICLES_PATH + "/{id}",
            SELLER_PATH + ARTICLES_PATH + "/{id}"})
    public String getArticleById(Model model, @PathVariable("id") Long id) throws ServiceException {
        ShowArticleDTO article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        if (!model.containsAttribute("changeArticle")) {
            model.addAttribute("changeArticle", new ChangeArticleDTO());
        }
        return "article";
    }

    @GetMapping(value = SELLER_PATH + ARTICLES_PATH + "/add")
    public String addPage(Model model) {
        model.addAttribute("addArticleDTO", new AddArticleDTO());
        return "add-article";
    }

    @PostMapping(value = SELLER_PATH + ARTICLES_PATH + "/add")
    public String add(@Valid AddArticleDTO addArticleDTO, BindingResult error) throws ServiceException {
        if (error.hasErrors()) {
            return "add-article";
        } else {
            articleService.add(addArticleDTO);
        }
        return "redirect:/seller/articles";
    }

    @GetMapping(value = SELLER_PATH + ARTICLES_PATH + "/{id}/delete")
    public String deleteArticle(@PathVariable("id") Long id) throws ServiceException {
        articleService.isDeleteById(id);
        return "redirect:/seller/articles";
    }

    @PostMapping(value = "/seller/articles/{id}/change-parameter")
    public String changeParameterById(@Valid @ModelAttribute("changeArticle") ChangeArticleDTO changeArticleDTO,
                                      BindingResult result,
                                      @PathVariable Long id,
                                      RedirectAttributes redirectAttributes) throws ServiceException {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changeArticle", result);
            redirectAttributes.addFlashAttribute("changeArticle", changeArticleDTO);
            return String.format("redirect:/seller/articles/%d", id);
        }
        articleService.changeParameterById(changeArticleDTO, id);
        return String.format("redirect:/seller/articles/%d", id);
    }
}