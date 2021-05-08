package ru.mail.senokosov.artem.service.model;

import ru.mail.senokosov.artem.repository.model.Status;
import ru.mail.senokosov.artem.repository.model.User;

import java.time.LocalDate;

public class ReviewDTO {

    private Long id;
    private String topic;
    private String review;
    private LocalDate localDate;
    private Status status;
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
