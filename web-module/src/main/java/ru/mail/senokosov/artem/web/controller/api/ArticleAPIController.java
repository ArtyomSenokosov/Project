package ru.mail.senokosov.artem.web.controller.api;

import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mail.senokosov.artem.service.ArticleService;
import ru.mail.senokosov.artem.service.model.ErrorDTO;
import ru.mail.senokosov.artem.service.model.add.AddArticleDTO;
import ru.mail.senokosov.artem.service.model.show.ShowArticleDTO;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class ArticleAPIController {

    private final ArticleService articleService;

    @GetMapping(value = "/articles")
    public List<ShowArticleDTO> getArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping(value = "/articles/{id}")
    public ShowArticleDTO getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @PostMapping(value = "/articles")
    public ResponseEntity<Object> addArticle(@RequestBody @Valid AddArticleDTO addArticleDTO,
                                             BindingResult result) {
        if (result.hasErrors()) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setErrors(result.getFieldErrors());
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        } else {
            articleService.persist(addArticleDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @DeleteMapping(value = "/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) throws ServiceException {
        boolean deleteById = articleService.deleteArticleById(id);
        if (deleteById) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
