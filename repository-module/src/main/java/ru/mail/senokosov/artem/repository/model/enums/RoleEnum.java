package ru.mail.senokosov.artem.repository.model.enums;

public enum RoleEnum {

    ADMINISTRATOR("Administrator"),
    SALE_USER("Sale user"),
    CUSTOMER_USER("Customer user"),
    SECURE_API_USER("Secure REST API");

    private final String description;

    RoleEnum(String description) {
        this.description = description;
    }
}
