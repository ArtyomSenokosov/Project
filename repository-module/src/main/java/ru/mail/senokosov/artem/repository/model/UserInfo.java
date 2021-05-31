package ru.mail.senokosov.artem.repository.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_details")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "address")
    private String address;
    @Column(name = "telephone")
    private String telephone;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
