package ru.mail.senokosov.artem.web.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mail.senokosov.artem.service.NewsService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.NewsDTO;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.util.ValidationUtil;

import javax.validation.Valid;

import static ru.mail.senokosov.artem.web.constant.PathConstant.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping(value = {CUSTOMER_PATH + NEWS_PATH,
            ADMIN_PATH + NEWS_PATH,
            SELLER_PATH + NEWS_PATH})
    public String getNewsByPagination(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        PageDTO pageDTO = newsService.getNewsByPage(page);
        model.addAttribute("pageDTO", pageDTO);
        log.info("Accessing news page with page number: {}", page);
        return "newses";
    }

    @GetMapping(value = {CUSTOMER_PATH + NEWS_PATH + "/{id}",
            ADMIN_PATH + NEWS_PATH + "/{id}",
            SELLER_PATH + NEWS_PATH + "/{id}"})
    public String getNewsById(Model model, @PathVariable("id") Long id) throws ServiceException {
        try {
            NewsDTO newsById = newsService.getNewsById(id);
            model.addAttribute("news", newsById);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .orElseThrow(() -> new ServiceException("User has no roles"))
                    .getAuthority();
            String username = authentication.getName();

            log.info("News with id {} was accessed by user {} with role {}", id, username, role);

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                model.addAttribute("userPath", "/admin");
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
                model.addAttribute("userPath", "/customer");
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SELLER"))) {
                model.addAttribute("userPath", "/seller");
            }

            return "news";
        } catch (ServiceException exception) {
            log.error("Error fetching news with id {}: {}", id, exception.getMessage());
            throw exception;
        }
    }

    @PostMapping(value = {SELLER_PATH + NEWS_PATH + "/delete",
            ADMIN_PATH + NEWS_PATH + "/delete"})
    public String deleteNews(@RequestParam(name = "newsId") Long newsId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRATOR"))) {
            newsService.isDeleteById(newsId);
            log.info("Administrator '{}' deleted news with id {}", username, newsId);
            return "redirect:" + ADMIN_PATH + NEWS_PATH;
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("SALE_USER"))) {
            newsService.isDeleteById(newsId);
            log.info("Sale user '{}' deleted news with id {}", username, newsId);
            return "redirect:" + SELLER_PATH + NEWS_PATH;
        } else {
            log.warn("Unauthorized attempt to delete news with id {} by user '{}'", newsId, username);
            return "redirect:" + ACCESS_DENIED_PATH;
        }
    }

    @GetMapping(value = {SELLER_PATH + NEWS_PATH + "/add-news",
            ADMIN_PATH + NEWS_PATH + "/add-news"})
    public String showAddNewsForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        String formActionUrl;

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRATOR"))) {
            formActionUrl = ADMIN_PATH + NEWS_PATH + "/add-news";
            log.info("Administrator '{}' is accessing the add news form", username);
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("SALE_USER"))) {
            formActionUrl = SELLER_PATH + NEWS_PATH + "/add-news";
            log.info("Seller '{}' is accessing the add news form", username);
        } else {
            log.warn("Unauthorized user '{}' attempted to access the add news form", username);
            return "redirect:" + ACCESS_DENIED_PATH;
        }

        model.addAttribute("news", new NewsDTO());
        model.addAttribute("fieldConstraints", ValidationUtil.getFieldSizeConstraints(NewsDTO.class));
        model.addAttribute("formActionUrl", formActionUrl);

        return "add-news";
    }

    @PostMapping(value = {SELLER_PATH + NEWS_PATH + "/add-news",
            ADMIN_PATH + NEWS_PATH + "/add-news"})
    public String addNews(@ModelAttribute("news") @Valid NewsDTO addNewsDTO,
                          BindingResult result, Authentication authentication) throws ServiceException {
        String username = authentication.getName();

        if (result.hasErrors()) {
            log.warn("User '{}' encountered validation errors while adding news", username);
            return "add-news";
        } else {
            newsService.addNews(addNewsDTO);
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRATOR"))) {
                log.info("Administrator '{}' added a new news item successfully", username);
                return "redirect:" + ADMIN_PATH + NEWS_PATH;
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("SALE_USER"))) {
                log.info("Seller '{}' added a new news item successfully", username);
                return "redirect:" + SELLER_PATH + NEWS_PATH;
            } else {
                log.warn("Unauthorized user '{}' attempted to add news", username);
                return "redirect:" + ACCESS_DENIED_PATH;
            }
        }
    }

    @PostMapping(value = {SELLER_PATH + NEWS_PATH + "/edit",
            ADMIN_PATH + NEWS_PATH + "/edit"})
    public String updateNews(@ModelAttribute("news") NewsDTO newsDTO, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            newsService.updateNews(newsDTO);
            redirectAttributes.addFlashAttribute("successMessage", "News updated successfully");
            log.info("User '{}' updated news with id {} successfully", username, newsDTO.getId());
        } catch (ServiceException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating news: "
                    + exception.getMessage());
            log.error("Error updating news with id {} by user '{}': {}", newsDTO.getId(), username, exception.getMessage());
        }

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRATOR"))) {
            return "redirect:" + ADMIN_PATH + NEWS_PATH + "/" + newsDTO.getId();
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("SALE_USER"))) {
            return "redirect:" + SELLER_PATH + NEWS_PATH + "/" + newsDTO.getId();
        } else {
            log.warn("Unauthorized attempt to update news with id {} by user '{}'", newsDTO.getId(), username);
            return "redirect:" + ACCESS_DENIED_PATH;
        }
    }
}