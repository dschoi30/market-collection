package com.marketcollection.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
@NoArgsConstructor
@Getter
@Embeddable
public class Address {

    private int zipCode;
    private String address;
    private String detailAddress;

    public Address(int zipCode, String address, String detailAddress) {
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
    }
}
