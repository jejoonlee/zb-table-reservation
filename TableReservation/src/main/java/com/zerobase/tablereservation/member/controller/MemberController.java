package com.zerobase.tablereservation.member.controller;

import com.zerobase.tablereservation.member.dto.Login;
import com.zerobase.tablereservation.member.dto.MemberRegister;
import com.zerobase.tablereservation.member.security.TokenProvider;
import com.zerobase.tablereservation.member.service.impl.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberServiceImpl memberServiceImpl;

    private final TokenProvider tokenProvider;

    // 회원가입
    @PostMapping("/register")
    public MemberRegister.Response register(
            @RequestBody MemberRegister.Request request
            ) {

        MemberRegister.Response memberRegisterResponse = memberServiceImpl.register(request);

        return memberRegisterResponse;
    }

    @PostMapping("/login")
    public String login(@RequestBody Login.Request request) {
        Login.Response response = memberServiceImpl.login(request);

        return "토큰이 생성이 되었습니다\n" + tokenProvider.generateToken(response.getUsername(), response.getOwnerOrCustomer());
    }

}
