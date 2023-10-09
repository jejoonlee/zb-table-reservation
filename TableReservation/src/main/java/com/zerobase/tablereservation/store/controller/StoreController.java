package com.zerobase.tablereservation.store.controller;

import com.zerobase.tablereservation.exception.ErrorCode;
import com.zerobase.tablereservation.exception.MemberException;
import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.store.dto.StoreCancelReserve;
import com.zerobase.tablereservation.store.dto.StoreDetailMessage;
import com.zerobase.tablereservation.store.dto.StoreMessage;
import com.zerobase.tablereservation.store.service.impl.StoreServiceImpl;
import com.zerobase.tablereservation.visitor.dto.ReserveRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreServiceImpl storeServiceImpl;

    private MemberEntity authenticate(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            throw new MemberException(ErrorCode.USER_NOT_LOGGED_IN);
        }

        return (MemberEntity) authentication.getPrincipal();
    }

    // http://localhost:8080/store/register
    @PostMapping("/register")
    @PreAuthorize("hasRole('STORE_OWNER')")
    public StoreMessage.Response registerStore(
            @RequestBody @Valid StoreMessage.Request request,
            Authentication authentication
    ) {

        MemberEntity member = authenticate(authentication);

        return storeServiceImpl.registerStore(request, member);
    }

    // http://localhost:8080/store/all
    @GetMapping("/all")
    @PreAuthorize("hasRole('STORE_OWNER')")
    public List<StoreMessage.Response> getAllRegisteredStores(
        Authentication authentication
    ) {

        MemberEntity member = authenticate(authentication);

        return storeServiceImpl.getAllRegisteredStores(member);
    }

    // http://localhost:8080/store/update
    @PutMapping ("/update")
    @PreAuthorize("hasRole('STORE_OWNER')")
    public StoreMessage.Response updateStore (
            @RequestBody @Valid StoreMessage.UpdateRequest request,
            Authentication authentication
    ) {

        MemberEntity member = authenticate(authentication);

        return storeServiceImpl.updateStore(request, member);
    }


    // http://localhost:8080/store/delete?storeNum={storeNumber}
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('STORE_OWNER')")
    public String deleteStore(
            @RequestParam Long storeNum,
            Authentication authentication
    ){

        MemberEntity member = authenticate(authentication);

        return storeServiceImpl.deleteStore(storeNum, member);
    }



    // ================= 매장 예약 관련 ========================

    // http://localhost:8080/store/reservations?storeId={storeId}
    @GetMapping("/reservations")
    @PreAuthorize("hasRole('STORE_OWNER')")
    public List<ReserveRecord.Response> getAllReservations (
            @RequestParam Long storeId,
            Authentication authentication
    ){
        MemberEntity member = authenticate(authentication);

        return storeServiceImpl.getAllReservations(storeId, member);
    }

    // http://localhost:8080/store/cancel-reservation
    @PostMapping("/cancel-reservation")
    @PreAuthorize("hasRole('STORE_OWNER')")
    public String cancelReservation(
            @RequestBody @Valid StoreCancelReserve.Request request,
            Authentication authentication
            ) {

        MemberEntity member = authenticate(authentication);

        return storeServiceImpl.cancelReservation(request, member);
    }

    // http://localhost:8080/store/search?keyword={keyword}
    @GetMapping("/search")
    public List<HashMap<String, String>> getAllStores (
            @RequestParam String keyword,
            final Pageable pageable
    ) {
        return storeServiceImpl.getAllStores(keyword);
    }

    // http://localhost:8080/store?storeId={storeId}
    @GetMapping()
    public StoreDetailMessage.Response getStore(
        @RequestParam Long storeId
    ){
        return storeServiceImpl.getStore(storeId);
    }


}
