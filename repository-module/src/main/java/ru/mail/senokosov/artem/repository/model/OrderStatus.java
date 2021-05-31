package ru.mail.senokosov.artem.repository.model;

import lombok.Data;
import ru.mail.senokosov.artem.repository.model.enums.OrderStatusEnum;

import javax.persistence.*;

@Data
@Entity
@Table(name = "order_status")
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;
}
