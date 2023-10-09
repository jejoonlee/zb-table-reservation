package com.zerobase.tablereservation.visitor.controller;

import com.zerobase.tablereservation.exception.ErrorCode;
import com.zerobase.tablereservation.exception.MemberException;
import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.visitor.dto.ReservationMessage;
import com.zerobase.tablereservation.visitor.dto.ReserveRecord;
import com.zerobase.tablereservation.visitor.dto.ReviewMessage;
import com.zerobase.tablereservation.visitor.service.impl.CustomerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerServiceImpl customerService;

    private MemberEntity authenticate(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            throw new MemberException(ErrorCode.USER_NOT_LOGGED_IN);
        }

        return (MemberEntity) authentication.getPrincipal();
    }

    // http://localhost:8080/customer/reserve
    @PostMapping("/reserve")
    @PreAuthorize("hasRole('STORE_USER')")
    public ReservationMessage.Response reserve(
            @RequestBody @Valid ReservationMessage.Request request,
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
            @RequestBody @Valid ReservationMessage.RequestUpdate request,
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


    // ============== Review CRUD ==================

    // http://localhost:8080/customer/review/write-review
    @PostMapping("/review/write")
    @PreAuthorize("hasRole('STORE_USER')")
    public ReviewMessage.Response writeReview (
            @RequestBody @Valid ReviewMessage.Request request,
            Authentication authentication
    ) {

        MemberEntity member = authenticate(authentication);

        return customerService.writeReview(request, member);
    }


    // http://localhost:8080/customer/review/read
    @GetMapping("/review/read")
    @PreAuthorize("hasRole('STORE_USER')")
    public List<ReviewMessage.Response> getAllUserReview(
            Authentication authentication
    ){

        MemberEntity member = authenticate(authentication);

        return customerService.getAllUserReview(member);
    }


    // http://localhost:8080/customer/review/update
    @PostMapping("/review/update")
    @PreAuthorize("hasRole('STORE_USER')")
    public ReviewMessage.Response updateReview(
        @RequestBody @Valid ReviewMessage.Request request,
        Authentication authentication
    ){
        MemberEntity member = authenticate(authentication);

        return customerService.updateReview(request, member);
    }

    // http://localhost:8080/customer/review/delete?reserveNum={reserveNum}
    // 리뷰 내용을 null로 바꾸는 것 밖에 없음 (삭제하는 것은 아님)
    @DeleteMapping("/review/delete")
    @PreAuthorize("hasRole('STORE_USER')")
    public String deleteReview(
        @RequestParam Long reserveNum,
        Authentication authentication
    ) {

        MemberEntity member = authenticate(authentication);

        return customerService.deleteReview(reserveNum, member);
    }


}
