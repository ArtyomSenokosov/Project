package ru.mail.senokosov.artem.repository.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "uuid")
    @Type(type = "uuid-char")
    private UUID uuid;
    @Column(name = "price")
    private Long price;
    @Column(name = "number")
    private Long number;
    @Column(name = "content")
    private String content;
}
