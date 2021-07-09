package ru.mail.senokosov.artem.repository.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Type(type = "uuid-char")
    @Column(name = "number_of_order")
    private UUID numberOfOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_status_id")
    private OrderStatus orderStatus;
    @Column(name = "number_of_items")
    private Long numberOfItems;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Column(name = "date")
    private LocalDateTime localDateTime;
    @OneToOne(fetch = FetchType.LAZY,
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OrderInfo orderInfo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}