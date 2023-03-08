package com.marketcollection.domain.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity{

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    @NoArgsConstructor
    @Getter
    @Embeddable
    public static class Address {

        private int zipCode;
        private String address;
        private String detailAddress;

        public Address(int zipCode, String address, String detailAddress) {
            this.zipCode = zipCode;
            this.address = address;
            this.detailAddress = detailAddress;
        }
    }
}
