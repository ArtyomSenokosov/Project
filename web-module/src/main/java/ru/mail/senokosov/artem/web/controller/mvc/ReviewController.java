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
import ru.mail.senokosov.artem.service.ReviewService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.ReviewDTO;

import javax.validation.Valid;

import static ru.mail.senokosov.artem.web.constant.PathConstant.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping(value = ADMIN_PATH + REVIEWS_PATH)
    public String getReviewsByPagination(Model model, @RequestParam(name = "page", defaultValue = "1") int page) {
        PageDTO pageDTO = reviewService.getReviewsByPage(page);
        model.addAttribute("pageDTO", pageDTO);
        log.info("Accessing reviews page with page number: {}", page);
        return "reviews";
    }

    @PostMapping(value = ADMIN_PATH + REVIEWS_PATH + "/{id}/change-status")
    public String changeReviewStatus(@PathVariable Long id) throws ServiceException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            reviewService.changeReviewStatus(id);
            log.info("User '{}' changed the status of review {}", username, id);
            return "redirect:" + ADMIN_PATH + REVIEWS_PATH;
        } catch (ServiceException exception) {
            log.error("Error changing status of review {} by user '{}': {}", id, username, exception.getMessage());
            throw exception;
        }
    }

    @PostMapping(value = ADMIN_PATH + REVIEWS_PATH + "/delete")
    public String deleteReview(@RequestParam(name = "reviewId") Long reviewId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            reviewService.isDeleteById(reviewId);
            log.info("User '{}' deleted review with id {}", username, reviewId);
            return "redirect:" + ADMIN_PATH + REVIEWS_PATH;
        } catch (Exception exception) {
            log.error("Error deleting review with id {} by user '{}': {}", reviewId, username, exception.getMessage());
            return "redirect:/error";
        }
    }

    @GetMapping(value = {CUSTOMER_PATH + REVIEWS_PATH + "/add-review",
            ADMIN_PATH + REVIEWS_PATH + "/add-review"})
    public String showAddReviewForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        log.info("User '{}' is accessing the add review form", username);

        model.addAttribute("reviewDTO", new ReviewDTO());
        return "add-review";
    }

    @PostMapping(value = {CUSTOMER_PATH + REVIEWS_PATH + "/add-review",
            ADMIN_PATH + REVIEWS_PATH + "/add-review"})
    public String addReview(@ModelAttribute("review") @Valid ReviewDTO addReviewDTO, BindingResult result, Authentication authentication) throws ServiceException {
        String username = authentication.getName();

        if (result.hasErrors()) {
            log.warn("User '{}' encountered validation errors while adding a review", username);
            return "add-review";
        }

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRATOR"))) {
            reviewService.addReview(addReviewDTO);
            log.info("Administrator '{}' added a review successfully", username);
            return "redirect:" + ADMIN_PATH + REVIEWS_PATH;
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("CUSTOMER_USER"))) {
            reviewService.addReview(addReviewDTO);
            log.info("Customer user '{}' added a review successfully", username);
            return "redirect:" + CUSTOMER_PATH + REVIEWS_PATH + "/add-review";
        } else {
            log.warn("Unauthorized attempt to add a review by user '{}'", username);
            return "redirect:" + ACCESS_DENIED_PATH;
        }
    }
}