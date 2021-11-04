package ru.mail.senokosov.artem.service.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.mail.senokosov.artem.service.model.PaginationResult;

class PaginationUtilTest {

    @Test
    void shouldCalculateTotalPagesCorrectlyForExactMultipleItems() {
        long totalCount = 100;
        int itemsPerPage = 10;
        int requestedPage = 5;

        PaginationResult result = PaginationUtil.calculatePagination(totalCount, itemsPerPage, requestedPage);

        Assertions.assertEquals(10, result.getTotalPages());
        Assertions.assertEquals(5, result.getCurrentPage());
        Assertions.assertEquals(40, result.getStartPosition());
    }

    @Test
    void shouldHandleLessItemsThanPerPageGracefully() {
        long totalCount = 5;
        int itemsPerPage = 10;
        int requestedPage = 1;

        PaginationResult result = PaginationUtil.calculatePagination(totalCount, itemsPerPage, requestedPage);

        Assertions.assertEquals(1, result.getTotalPages());
        Assertions.assertEquals(1, result.getCurrentPage());
        Assertions.assertEquals(0, result.getStartPosition());
    }

    @Test
    void shouldAdjustToLastPageWhenRequestedPageIsGreaterThanTotalPages() {
        long totalCount = 45;
        int itemsPerPage = 10;
        int requestedPage = 6;

        PaginationResult result = PaginationUtil.calculatePagination(totalCount, itemsPerPage, requestedPage);

        Assertions.assertEquals(5, result.getTotalPages());
        Assertions.assertEquals(5, result.getCurrentPage());
        Assertions.assertEquals(40, result.getStartPosition());
    }

    @Test
    void shouldDefaultToFirstPageWhenRequestedPageIsLessThanOne() {
        long totalCount = 45;
        int itemsPerPage = 10;
        int requestedPage = 0;

        PaginationResult result = PaginationUtil.calculatePagination(totalCount, itemsPerPage, requestedPage);

        Assertions.assertEquals(5, result.getTotalPages());
        Assertions.assertEquals(1, result.getCurrentPage());
        Assertions.assertEquals(0, result.getStartPosition());
    }
}