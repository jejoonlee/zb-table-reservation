package com.zerobase.tablereservation.store.controller;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.member.security.TokenProvider;
import com.zerobase.tablereservation.store.dto.StoreRegister;
import com.zerobase.tablereservation.store.service.impl.StoreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreServiceImpl storeServiceImpl;
    private final TokenProvider tokenProvider;

    @PostMapping("/register")
    @PreAuthorize("hasRole('STORE_OWNER')")
    public StoreRegister.Response registerStore(
            @RequestBody StoreRegister.Request request,
            Authentication authentication
    ) {
        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("로그인이 제대로 안 되어 있습니다");
        }

        MemberEntity member = (MemberEntity) authentication.getPrincipal();

        return storeServiceImpl.registerStore(request, member);
    }

    @GetMapping("/test")
    public void test(
            Authentication authentication
    ){

        System.out.println(authentication.getName());

    }


}
