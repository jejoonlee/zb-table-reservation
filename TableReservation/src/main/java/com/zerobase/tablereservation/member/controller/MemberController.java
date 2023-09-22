package com.zerobase.tablereservation.member.controller;

import com.zerobase.tablereservation.member.dto.MemberRegister;
import com.zerobase.tablereservation.member.service.impl.MemberServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    MemberServiceImpl memberServiceImpl;

    // 회원가입
    @PostMapping("/register")
    public MemberRegister.Response register(
            @RequestBody MemberRegister.Request request
            ) {

        MemberRegister.Response memberRegisterResponse = memberServiceImpl.register(request);

        return memberRegisterResponse;
    }

}
