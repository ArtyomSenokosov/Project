package ru.mail.senokosov.artem.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.senokosov.artem.repository.model.Article;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.ArticleService;
import ru.mail.senokosov.artem.service.model.add.AddArticleDTO;
import ru.mail.senokosov.artem.service.model.add.AddCommentDTO;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Controller
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping(value = "/articles/page/{pageNo}")
    public String getArticlesByPagination(@PathVariable(value = "pageNo") int pageNo,
                                          @RequestParam("sortField") String sortField,
                                          @RequestParam("sortDir") String sortDir,
                                          Model model) {
        Integer pageSize = 10;

        Page<ShowArticleDTO> page = articleService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<ShowArticleDTO> articles = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listArticles", articles);
        return "articles";
    }

    @GetMapping(value = "articles/{id}")
    public String getArticleById(Model model, @PathVariable("id") Long id) {
        ShowArticleDTO article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping(value = "/seller/articles/add")
    public String addPage(Model model) {
        model.addAttribute("article", new AddArticleDTO());
        return "add-article";
    }

    @PostMapping(value = "/seller/articles/add")
    public String add(@Valid AddArticleDTO addArticleDTO, BindingResult error) throws SecurityException {
        if (error.hasErrors()) {
            log.info("errors:{}", error);
            return "add-article";
        } else {
            articleService.persist(addArticleDTO);
            log.info("go persist article");
        }
        return "redirect:/articles";
    }
}
