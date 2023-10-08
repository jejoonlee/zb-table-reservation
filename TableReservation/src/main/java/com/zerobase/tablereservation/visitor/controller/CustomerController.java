package com.zerobase.tablereservation.visitor.controller;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.visitor.dto.ReservationMessage;
import com.zerobase.tablereservation.visitor.dto.ReserveRecord;
import com.zerobase.tablereservation.visitor.dto.WriteReview;
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
    public ReservationMessage.Response reserve(
            @RequestBody ReservationMessage.Request request,
            Authentication authentication
    ) {

        MemberEntity member = authenticate(authentication);

        return customerService.makeReservation(request, member);
    }

    // http://localhost:8080/customer/my-reservations
    @GetMapping("/my-reservations")
    @PreAuthorize("hasRole('STORE_USER')")
    public List<ReserveRecord.Response> getMyReservations (
            Authentication authentication
    ) {

        MemberEntity member = authenticate(authentication);

        return customerService.getMyReservations(member);
    }

    // http://localhost:8080/customer/reserve/update
    @PutMapping("/reserve/update")
    @PreAuthorize("hasRole('STORE_USER')")
    public ReservationMessage.Response reserveUpdate(
            @RequestBody ReservationMessage.RequestUpdate request,
        Authentication authentication
    ) {

        MemberEntity member = authenticate(authentication);

        return customerService.updateReservation(request, member);
    }

    // http://localhost:8080/customer/reserve/cancel?reserveNum={reserveNum}
    @DeleteMapping("/reserve/cancel")
    @PreAuthorize("hasRole('STORE_USER')")
    public String reserveDelete (
            @RequestParam Long reserveNum,
            Authentication authentication
    ){
        MemberEntity member = authenticate(authentication);
        return customerService.cancelReservation(reserveNum, member);
    }

    // http://localhost:8080/customer/write-review
    @PostMapping("/write-review")
    @PreAuthorize("hasRole('STORE_USER')")
    public WriteReview.Response writeReview (
            @RequestBody WriteReview.Request request,
            Authentication authentication
    ) {

        MemberEntity member = authenticate(authentication);

        return customerService.writeReview(request, member);
    }

}
