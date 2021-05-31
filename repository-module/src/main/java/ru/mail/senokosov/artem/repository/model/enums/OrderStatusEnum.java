package ru.mail.senokosov.artem.repository.model.enums;

public enum OrderStatusEnum {

    NEW("New"),
    IN_PROGRESS("In progress"),
    DELIVERED("Delivered"),
    REJECTED("Rejected");

    private final String description;

    OrderStatusEnum(String description) {
        this.description = description;
    }
}
