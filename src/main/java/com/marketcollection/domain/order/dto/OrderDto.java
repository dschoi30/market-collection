package com.marketcollection.domain.order.dto;

import com.marketcollection.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@ToString
@NoArgsConstructor
@Getter @Setter
public class OrderDto {
    @NotBlank @Size(max = 20, message = "주문자명은 필수 입력 값입니다.")
    private String memberName;

    @Min(value = 10)
    private int phoneNumber;

    @Min(value = 5)
    private int zipCode;

    @NotBlank
    private String address;

    @NotBlank
    private String detailAddress;

    private List<OrderItemDto> orderItemDtos;
    private String directOrderYn;

    public void setMemberInfo(Member member) {
        this.memberName = member.getMemberName();
        this.phoneNumber = member.getPhoneNumber();
        this.zipCode = member.getAddress().getZipCode();
        this.address = member.getAddress().getAddress();
        this.detailAddress = member.getAddress().getDetailAddress();
    }
}
