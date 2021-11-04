package ru.mail.senokosov.artem.service.model;

import lombok.Data;

@Data
public class PaginationResult {

    private final long totalPages;
    private final int currentPage;
    private final int startPosition;
}
