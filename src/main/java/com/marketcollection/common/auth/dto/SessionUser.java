package com.marketcollection.common.auth.dto;

import com.marketcollection.domain.member.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private String email;
    private String userName;

    public SessionUser(Member member) {
        this.email = member.getEmail();
        this.userName = member.getMemberName();
    }
}
