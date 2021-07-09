package ru.mail.senokosov.artem.service.model;

import lombok.Data;
import ru.mail.senokosov.artem.service.model.show.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDTO {

    private Long countOfPages;
    private Long currentPage;
    private Long beginPage;
    private Long endPage;
    private List<ShowUserDTO> users = new ArrayList<>();
    private List<ShowReviewDTO> reviews = new ArrayList<>();
    private List<ShowArticleDTO> articles = new ArrayList<>();
    private List<ShowItemDTO> items = new ArrayList<>();
    private List<ShowOrderDTO> orders = new ArrayList<>();
    private Integer startPosition;
}