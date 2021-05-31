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
import ru.mail.senokosov.artem.service.ReviewService;
import ru.mail.senokosov.artem.service.model.add.AddReviewDTO;
import ru.mail.senokosov.artem.service.model.show.ShowReviewDTO;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping(value = "/reviews/page/{pageNo}")
    public String getArticlesByPagination(@PathVariable(value = "pageNo") int pageNo,
                                          @RequestParam("sortField") String sortField,
                                          @RequestParam("sortDir") String sortDir,
                                          Model model) {
        Integer pageSize = 10;

        Page<ShowReviewDTO> page = reviewService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<ShowReviewDTO> reviews = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listReviews", reviews);
        return "reviews";
    }

    @GetMapping(value = "/customer/review/add")
    public String addPage(Model model) {
        model.addAttribute("review", new AddReviewDTO());
        return "add-review";
    }

    @PostMapping(value = "/customer/review/add")
    public String add(@Valid AddReviewDTO addReviewDTO, BindingResult error,
                      @RequestParam(value = "username") String userName,
                      @RequestParam(name = "review") Long id) throws SecurityException {
        if (error.hasErrors()) {
            log.info("errors:{}", error);
            return "add-review";
        } else {
            reviewService.persist(addReviewDTO);
            log.info("go persist comment");
        }
        return "redirect:/reviews";
    }
}
