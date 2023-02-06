package com.marketcollection.domain.member;

import com.marketcollection.api.common.BaseEntity;
import com.marketcollection.common.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
