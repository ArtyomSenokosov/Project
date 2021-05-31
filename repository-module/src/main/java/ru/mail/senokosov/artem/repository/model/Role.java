package ru.mail.senokosov.artem.repository.model;

import lombok.Data;
import ru.mail.senokosov.artem.repository.model.enums.RoleEnum;

import javax.persistence.*;

@Data
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}
