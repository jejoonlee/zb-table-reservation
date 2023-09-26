package com.zerobase.tablereservation.member.service;


import com.zerobase.tablereservation.member.dto.Login;
import com.zerobase.tablereservation.member.dto.MemberRegister;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {

    MemberRegister.Response register(MemberRegister.Request request);

    Login.Response login(Login.Request request);
}
