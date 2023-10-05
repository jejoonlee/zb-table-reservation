package com.zerobase.tablereservation.visitor.controller;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.visitor.dto.CustomerReserveRegister;
import com.zerobase.tablereservation.visitor.service.impl.CustomerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerServiceImpl customerService;

    // http://localhost:8080/customer/reserve
    @PostMapping("/reserve")
    @PreAuthorize("hasRole('STORE_USER')")
    public CustomerReserveRegister.Response reserve(
            @RequestBody CustomerReserveRegister.Request request,
            Authentication authentication
    ) {
        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("로그인이 제대로 안 되어 있습니다");
        }
        MemberEntity member = (MemberEntity) authentication.getPrincipal();

        return customerService.makeReservation(request, member);
    }

    @PostMapping("/test")
    public LocalDateTime test(
            @RequestBody CustomerReserveRegister.Request request
    ) {
        return request.getReserveDate();
    }
}
