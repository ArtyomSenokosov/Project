package ru.mail.senokosov.artem.repository.model.enums;

public enum ReviewStatusEnum {

    VISIBLE("visible"),
    INVISIBLE("invisible");

    private final String description;

    ReviewStatusEnum(String description) {
        this.description = description;
    }
}
