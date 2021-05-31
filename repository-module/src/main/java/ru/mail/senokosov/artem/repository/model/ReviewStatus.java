package ru.mail.senokosov.artem.repository.model;

import lombok.Data;
import ru.mail.senokosov.artem.repository.model.enums.ReviewStatusEnum;

import javax.persistence.*;

@Data
@Entity
@Table(name = "review_status")
public class ReviewStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReviewStatusEnum status;
}