package ru.mail.senokosov.artem.web.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mail.senokosov.artem.service.NewsService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.NewsDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.mail.senokosov.artem.web.constant.PathConstant.NEWS_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@Slf4j
@RestController
@RequestMapping(REST_API_USER_PATH)
@RequiredArgsConstructor
public class NewsAPIController {

    private final NewsService newsService;

    @GetMapping(value = NEWS_PATH)
    public ResponseEntity<List<NewsDTO>> getNews() {
        try {
            List<NewsDTO> newsDTOs = newsService.getAllNews();
            if (newsDTOs.isEmpty()) {
                log.info("No news articles found.");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(newsDTOs, HttpStatus.OK);
        } catch (Exception exception) {
            log.error("Error retrieving news: {}", exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = NEWS_PATH + "/{id}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable Long id) {
        try {
            NewsDTO newsById = newsService.getNewsById(id);
            return new ResponseEntity<>(newsById, HttpStatus.OK);
        } catch (ServiceException exception) {
            log.warn("News article with id: {} was not found.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            log.error("Error retrieving news by id: {}", exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = NEWS_PATH)
    public ResponseEntity<?> addNews(@RequestBody @Valid NewsDTO newsDTO, BindingResult result) {
        log.info("Attempting to add news: {}", newsDTO.getTitle());
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(Map.of("errors", errors), HttpStatus.BAD_REQUEST);
        }
        try {
            NewsDTO createdNews = newsService.addNews(newsDTO);
            log.info("Successfully added news with title: '{}'", createdNews.getTitle());
            return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
        } catch (ServiceException exception) {
            log.error("Error adding news with title '{}': {}", newsDTO.getTitle(), exception.getMessage());
            return new ResponseEntity<>(Map.of("error", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = NEWS_PATH + "/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        try {
            boolean isDeleted = newsService.isDeleteById(id);
            if (isDeleted) {
                String successMessage = "News article with id " + id + " was successfully deleted.";
                log.info(successMessage);
                return ResponseEntity.ok(Map.of("message", successMessage));
            } else {
                String notFoundMessage = "News article with id " + id + " was not found.";
                log.warn(notFoundMessage);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", notFoundMessage));
            }
        } catch (Exception exception) {
            String errorMessage = "Error deleting news article with id " + id + ": " + exception.getMessage();
            log.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", errorMessage));
        }
    }
}