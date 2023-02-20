package com.marketcollection.domain.order.dto;

import com.marketcollection.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
@Getter @Setter
public class OrderDto {
    private String memberName;
    private int phoneNumber;
    private int zipCode;
    private String address;
    private String detailAddress;
    private List<OrderItemDto> orderItemDtos;
    private boolean isDirectOrder;

    public void setMemberInfo(Member member) {
        this.memberName = member.getMemberName();
        this.phoneNumber = member.getPhoneNumber();
        this.zipCode = member.getAddress().getZipCode();
        this.address = member.getAddress().getAddress();
        this.detailAddress = member.getAddress().getDetailAddress();
    }
}
