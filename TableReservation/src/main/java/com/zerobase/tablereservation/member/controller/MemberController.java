package com.zerobase.tablereservation.member.controller;

import com.zerobase.tablereservation.member.dto.Login;
import com.zerobase.tablereservation.member.dto.MemberRegister;
import com.zerobase.tablereservation.member.security.TokenProvider;
import com.zerobase.tablereservation.member.service.impl.MemberServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberServiceImpl memberServiceImpl;

    private final TokenProvider tokenProvider;

    // 회원가입
    @PostMapping("/register")
    @ApiOperation(value="회원가입", notes="role은 0 또는 1이다\n0 : 매장 주인\n1: 매장 이용자")
    public MemberRegister.Response register(
            @RequestBody @Valid MemberRegister.Request request
            ) {

        MemberRegister.Response memberRegisterResponse = memberServiceImpl.register(request);

        return memberRegisterResponse;
    }

    @PostMapping("/login")
    @ApiOperation(value="로그인")
    public String login(@RequestBody @Valid Login.Request request) {
        Login.Response response = memberServiceImpl.login(request);

        return "토큰이 생성이 되었습니다\n" + tokenProvider.generateToken(response.getUsername(), response.getOwnerOrCustomer());
    }

}
