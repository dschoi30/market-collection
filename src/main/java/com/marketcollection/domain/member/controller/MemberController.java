package com.marketcollection.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/member")
@Controller
public class MemberController {

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }
}
