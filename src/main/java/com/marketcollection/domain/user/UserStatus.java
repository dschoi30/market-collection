package com.marketcollection.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {
    ACTIVE("가입 중 회원"), INACTIVE("탈퇴 회원"), SUSPENDED("정지 회원");

    private final String status;
}
