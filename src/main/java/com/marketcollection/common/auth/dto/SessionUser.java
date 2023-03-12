package com.marketcollection.common.auth.dto;

import com.marketcollection.domain.member.Grade;
import com.marketcollection.domain.member.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private String email;
    private String userName;
    private Grade grade;

    public SessionUser(Member member) {
        this.email = member.getEmail();
        this.userName = member.getMemberName();
        this.grade = member.getGrade();
    }
}
