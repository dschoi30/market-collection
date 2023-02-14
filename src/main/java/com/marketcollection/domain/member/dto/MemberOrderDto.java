package com.marketcollection.domain.member.dto;

import com.marketcollection.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class MemberOrderDto {
    private String memberName;
    private int phoneNumber;
    private int zipCode;
    private String address;
    private String detailAddress;

    private static ModelMapper modelMapper = new ModelMapper();

    public MemberOrderDto(Member member) {
        this.memberName = member.getMemberName();
        this.phoneNumber = member.getPhoneNumber();
        this.zipCode = member.getAddress().getZipCode();
        this.address = member.getAddress().getAddress();
        this.detailAddress = member.getAddress().getDetailAddress();
    }

    public static MemberOrderDto of(Member member) {
        return modelMapper.map(member, MemberOrderDto.class);
    }
}
