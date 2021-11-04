package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.CommentRepository;
import ru.mail.senokosov.artem.repository.NewsRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.News;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.service.NewsService;
import ru.mail.senokosov.artem.service.converter.NewsConverter;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.NewsDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.PaginationResult;
import ru.mail.senokosov.artem.service.util.PaginationUtil;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.mail.senokosov.artem.service.constant.FormatConstant.DATE_FORMAT_PATTERN;
import static ru.mail.senokosov.artem.service.constant.NewsConstant.MAXIMUM_NEWS_ON_PAGE;
import static ru.mail.senokosov.artem.service.util.SecurityUtil.getAuthentication;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final NewsConverter newsConverter;

    @Override
    @Transactional
    public PageDTO getNewsByPage(Integer page) {
        log.debug("Requesting page number {} for news.", page);

        Long countNews = newsRepository.getCount();

        PaginationResult pagination = PaginationUtil.calculatePagination(countNews, MAXIMUM_NEWS_ON_PAGE, page);

        List<News> news = newsRepository.findAll(pagination.getStartPosition(), MAXIMUM_NEWS_ON_PAGE);
        log.debug("Retrieved {} news for page {}.", news.size(), pagination.getCurrentPage());

        List<NewsDTO> newsDTOs = news.stream()
                .map(newsConverter::convert)
                .collect(Collectors.toList());

        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotalPages(pagination.getTotalPages());
        pageDTO.setCurrentPage((long) pagination.getCurrentPage());
        pageDTO.setNewses(newsDTOs);
        pageDTO.setStartPosition(pagination.getStartPosition());

        log.info("Page {} of news served with {} news.", pagination.getCurrentPage(), newsDTOs.size());
        return pageDTO;
    }

    @Override
    @Transactional
    public NewsDTO getNewsById(Long id) throws ServiceException {
        log.debug("Attempting to find news article by id: {}", id);

        News news = newsRepository.findById(id);
        if (Objects.nonNull(news)) {
            log.info("News article with id: {} found and being converted.", id);
            return newsConverter.convert(news);
        } else {
            log.warn("News article with id: {} was not found.", id);
            throw new ServiceException(String.format("News article with id: %s was not found", id));
        }
    }

    @Override
    @Transactional
    public List<NewsDTO> getAllNews() throws ServiceException {
        log.debug("Requesting all news articles.");

        try {
            List<News> news = newsRepository.findAll();

            if (news.isEmpty()) {
                log.info("No news articles were found.");
                return Collections.emptyList();
            }

            List<NewsDTO> newsDTOs = news.stream()
                    .map(newsConverter::convert)
                    .collect(Collectors.toList());

            log.info("Retrieved {} news articles.", newsDTOs.size());
            return newsDTOs;
        } catch (Exception exception) {
            log.error("An error occurred while fetching all news articles: {}", exception.getMessage());
            throw new ServiceException("An error occurred while fetching all news articles.");
        }
    }

    @Override
    @Transactional
    public boolean isDeleteById(Long id) {
        try {
            if (Objects.nonNull(newsRepository.findById(id))) {
                commentRepository.deleteByNewsId(id);
                log.debug("All comments related to news with id {} have been deleted.", id);

                newsRepository.removeById(id);
                log.info("News with id {} has been successfully deleted.", id);
                return true;
            } else {
                log.warn("News with id {} not found. Deletion cannot be performed.", id);
                return false;
            }
        } catch (Exception exception) {
            log.error("Error occurred during the deletion of news with id {}: {}", id, exception.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public NewsDTO addNews(NewsDTO newsDTO) throws ServiceException {
        log.debug("Starting process to add a new news article with title: {}", newsDTO.getTitle());

        Authentication authentication = getAuthentication();
        if (Objects.nonNull(authentication)) {
            String userName = authentication.getName();
            User user = userRepository.findUserByEmail(userName);

            if (Objects.nonNull(user)) {
                News news = newsConverter.convert(newsDTO);
                news.setDateOfCreation(LocalDateTime.now());
                news.setUser(user);

                newsRepository.persist(news);

                log.info("News article titled '{}' has been successfully added by user: {}",
                        newsDTO.getTitle(), userName);
            } else {
                log.error("User with username: {} was not found, unable to add news article.", userName);
                throw new ServiceException(String.format("User with username: %s was not found", userName));
            }
        } else {
            log.error("An attempt was made to add a news article without authentication. Title of the news article: {}",
                    newsDTO.getTitle());
            throw new ServiceException(String.format("A user without authentication tries to add a news article with title: %s",
                    newsDTO.getTitle()));
        }
        return newsDTO;
    }

    @Override
    @Transactional
    public void updateNews(NewsDTO newsDTO) throws ServiceException {
        log.debug("Starting update for news with id: {}", newsDTO.getId());

        News news = newsRepository.findById(newsDTO.getId());
        if (Objects.isNull(news)) {
            log.error("News not found with id: {}", newsDTO.getId());
            throw new ServiceException("News not found with id: " + newsDTO.getId());
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
            LocalDateTime date = LocalDateTime.parse(newsDTO.getDateOfCreation(), formatter);

            news.setLastDateUpdate(date);
            news.setTitle(newsDTO.getTitle());
            news.setContent(newsDTO.getFullContent());

            newsRepository.merge(news);

            log.info("Successfully updated news with id: {}", newsDTO.getId());
        } catch (DateTimeParseException exception) {
            log.error("Error parsing date for news with id: {}, error: {}", newsDTO.getId(), exception.getMessage());
            throw new ServiceException("Error parsing date of creation for news with id: " + newsDTO.getId());
        } catch (Exception exception) {
            log.error("Unexpected error occurred while updating news with id: {}, error: {}",
                    newsDTO.getId(), exception.getMessage());
            throw new ServiceException("Unexpected error occurred while updating news: " + exception.getMessage());
        }
    }
}