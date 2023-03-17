package com.marketcollection.domain.order.dto;

import com.marketcollection.domain.member.Member;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderDto {
    @NotBlank @Size(max = 20, message = "주문자명은 필수 입력 값입니다.")
    private String memberName;

    @Min(value = 10)
    private String phoneNumber;

    @Min(value = 5)
    private int zipCode;

    @NotBlank
    private String address;

    @NotBlank
    private String detailAddress;

    private int existingPoint;

    private int totalOrderAmount;
    private int totalSavingPoint;
    private int usingPoint;
    private int totalPaymentAmount;

    private List<OrderItemDto> orderItemDtos = new ArrayList<>();
    private String directOrderYn;

    public void setMemberInfo(Member member) {
        this.memberName = member.getMemberName();
        this.phoneNumber = member.getPhoneNumber();
        this.zipCode = member.getAddress().getZipCode();
        this.address = member.getAddress().getAddress();
        this.detailAddress = member.getAddress().getDetailAddress();
        this.existingPoint = member.getPoint();
    }

    public void setOrderItemInfo(List<OrderItemDto> orderItemDtos, int totalOrderAmount, int totalSavingPoint) {
        this.orderItemDtos = orderItemDtos;
        this.totalOrderAmount = totalOrderAmount;
        this.totalSavingPoint = totalSavingPoint;
    }

    public void setDirectOrderYn(String directOrderYn) {
        this.directOrderYn = directOrderYn;
    }
}
