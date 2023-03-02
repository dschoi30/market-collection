package com.marketcollection.domain.member;

import com.marketcollection.domain.common.BaseEntity;
import com.marketcollection.common.entity.Address;
import com.marketcollection.domain.order.dto.OrderDto;
import lombok.*;

import javax.persistence.*;
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String email;
    private String memberName;
    private int phoneNumber;

    @Embedded
    private Address address;
    private int point;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    public Member update(String memberName) {
        this.memberName = memberName;
        return this;
    }

    public void updateOrderInfo(OrderDto orderDto) {
        this.phoneNumber = orderDto.getPhoneNumber();
        this.address = new Address(orderDto.getZipCode(), orderDto.getAddress(), orderDto.getDetailAddress());
    }
}
