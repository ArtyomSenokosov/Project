package ru.mail.senokosov.artem.service.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDTO {

    private Long totalPages;
    private Long currentPage;
    private Long beginPage;
    private Long endPage;
    private Integer startPosition;
    private List<UserDTO> users = new ArrayList<>();
    private List<ReviewDTO> reviews = new ArrayList<>();
    private List<NewsDTO> newses = new ArrayList<>();
    private List<ItemDTO> items = new ArrayList<>();
    private List<OrderDTO> orders = new ArrayList<>();
}