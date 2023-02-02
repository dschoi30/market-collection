package com.marketcollection.domain.cart;

import com.marketcollection.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
