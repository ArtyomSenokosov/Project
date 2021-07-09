package ru.mail.senokosov.artem.web.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import java.util.Objects;

import static ru.mail.senokosov.artem.web.constant.PathConstant.ARTICLES_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@RestController
@RequestMapping(REST_API_USER_PATH)
@RequiredArgsConstructor
@Log4j2
public class ArticleAPIController {

    private final ArticleService articleService;

    @GetMapping(value = ARTICLES_PATH)
    public List<ShowArticleDTO> getArticles() {
        return articleService.getArticles();
    }

    @SneakyThrows
    @GetMapping(value = ARTICLES_PATH + "/{id}")
    public ResponseEntity<ShowArticleDTO> getArticleById(@PathVariable Long id) throws ServiceException {
        ShowArticleDTO articleById = articleService.getArticleById(id);
        if (Objects.nonNull(articleById)) {
            return new ResponseEntity<>(articleById, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @SneakyThrows
    @PostMapping(value = ARTICLES_PATH)
    public ResponseEntity<Object> addArticle(@RequestBody @Valid AddArticleDTO addArticleDTO,
                                             BindingResult result) throws ServiceException {
        if (result.hasErrors()) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setErrors(result.getFieldErrors());
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        } else {
            articleService.add(addArticleDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @DeleteMapping(value = ARTICLES_PATH + "/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) throws ServiceException {
        boolean deleteById = articleService.isDeleteById(id);
        if (deleteById) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}