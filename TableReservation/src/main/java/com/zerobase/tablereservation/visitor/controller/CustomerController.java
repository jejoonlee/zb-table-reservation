package com.zerobase.tablereservation.visitor.controller;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.visitor.dto.CustomerReserveRegister;
import com.zerobase.tablereservation.visitor.dto.ReserveRecord;
import com.zerobase.tablereservation.visitor.service.impl.CustomerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerServiceImpl customerService;

    private MemberEntity authenticate(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("로그인이 제대로 안 되어 있습니다");
        }

        return (MemberEntity) authentication.getPrincipal();
    }

    // http://localhost:8080/customer/reserve
    @PostMapping("/reserve")
    @PreAuthorize("hasRole('STORE_USER')")
    public CustomerReserveRegister.Response reserve(
            @RequestBody CustomerReserveRegister.Request request,
            Authentication authentication
    ) {

        MemberEntity member = authenticate(authentication);

        return customerService.makeReservation(request, member);
    }

    // http://localhost:8080/customer/my_reservations
    @GetMapping("/my_reservations")
    @PreAuthorize("hasRole('STORE_USER')")
    public List<ReserveRecord.Response> getMyReservations (
            Authentication authentication
    ) {

        MemberEntity member = authenticate(authentication);

        return customerService.getMyReservations(member);
    }

}
