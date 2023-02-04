package com.marketcollection.domain.cart;

import com.marketcollection.api.common.BaseEntity;
import com.marketcollection.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Cart extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
