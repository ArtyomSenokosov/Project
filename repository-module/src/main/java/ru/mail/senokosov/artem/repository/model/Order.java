package ru.mail.senokosov.artem.repository.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "order_number")
    private Long orderNumber;
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_status_id")
    private OrderStatus orderStatus;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "item_id")
    private Item item;
    @Column(name = "quantity")
    private Long quantity;
    @Column(name = "total_price")
    private Long totalPrice;
    @ManyToOne(optional = false)
    @JoinTable(name = "user_id")
    private User user;
    @ManyToOne(optional = false)
    @JoinTable(name = "user_details_id")
    private UserInfo userInfo;
}
