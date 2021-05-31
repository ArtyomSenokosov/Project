package ru.mail.senokosov.artem.repository.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "topic")
    private String topic;
    @Column(name = "review")
    private String review;
    @Column(name = "date")
    private LocalDateTime localDate;
    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id")
    private ReviewStatus reviewStatus;
    @ManyToOne(optional = false)
    @JoinTable(name = "re_user")
    private User user;
}
