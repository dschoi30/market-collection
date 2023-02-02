package com.marketcollection.common.entity;

import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Address {
    private int zipCode;
    private String street;
    private String comment;
}
