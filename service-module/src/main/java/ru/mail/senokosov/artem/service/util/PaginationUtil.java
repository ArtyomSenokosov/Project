package ru.mail.senokosov.artem.service.util;

import ru.mail.senokosov.artem.service.model.PaginationResult;

public class PaginationUtil {

    public static PaginationResult calculatePagination(long totalCount, int itemsPerPage, int requestedPage) {
        long totalPages = (totalCount + itemsPerPage - 1) / itemsPerPage;
        int currentPage = Math.max(1, Math.min(requestedPage, (int) totalPages));
        int startPosition = (currentPage - 1) * itemsPerPage;

        return new PaginationResult(totalPages, currentPage, startPosition);
    }
}
