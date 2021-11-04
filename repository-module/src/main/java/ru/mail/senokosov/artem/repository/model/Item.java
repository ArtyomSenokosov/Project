package ru.mail.senokosov.artem.repository.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Type(type = "uuid-char")
    @Column(name = "unique_number", nullable = false)
    private UUID uuid;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(nullable = false, length = 200)
    private String content;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
}