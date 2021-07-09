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
import ru.mail.senokosov.artem.service.ReviewService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.add.AddReviewDTO;

import javax.validation.Valid;
import java.util.List;

import static ru.mail.senokosov.artem.web.constant.PathConstant.*;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping(value = ADMIN_PATH + REVIEWS_PATH)
    public String getReviews(Model model, @RequestParam(name = "page", defaultValue = "1") int page) {
        PageDTO pageDTO = reviewService.getReviewsByPage(page);
        model.addAttribute("pageDTO", pageDTO);
        return "reviews";
    }

    @GetMapping(value = {CUSTOMER_PATH + SHOW_REVIEWS_PATH,
            SELLER_PATH + SHOW_REVIEWS_PATH})
    public String getShowReviews(Model model, @RequestParam(name = "page", defaultValue = "1") int page) {
        PageDTO pageDTO = reviewService.getShowReviewsByPage(page);
        model.addAttribute("pageDTO", pageDTO);
        return "show-reviews";
    }

    @GetMapping(value = ADMIN_PATH + REVIEWS_PATH + "/{id}/delete")
    public String deleteReviews(@PathVariable("id") Long id) {
        reviewService.deleteById(id);
        return "redirect:/admin/reviews";
    }

    @PostMapping(value = ADMIN_PATH + REVIEWS_PATH + "/show")
    public String showReviews(@RequestParam(value = "checkedIds", required = false) List<Long> checkedIds,
                              @RequestParam("allIds") List<Long> allIds) {
        reviewService.changeStatusByIds(checkedIds, allIds);
        return "redirect:/admin/reviews";
    }

    @GetMapping(value = CUSTOMER_PATH + REVIEWS_PATH + "/add")
    public String addPage(Model model) {
        model.addAttribute("addReviewDTO", new AddReviewDTO());
        return "add-review";
    }

    @PostMapping(value = CUSTOMER_PATH + REVIEWS_PATH + "/add")
    public String add(@Valid AddReviewDTO addReviewDTO, BindingResult error) throws ServiceException {
        if (error.hasErrors()) {
            return "add-review";
        } else {
            reviewService.add(addReviewDTO);
        }
        return String.format("redirect:%s%s", CUSTOMER_PATH, SHOW_REVIEWS_PATH);
    }
}