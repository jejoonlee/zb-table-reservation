package com.zerobase.tablereservation.store.controller;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.store.dto.StoreRegister;
import com.zerobase.tablereservation.store.dto.StoreSearch;
import com.zerobase.tablereservation.store.service.impl.StoreServiceImpl;
import com.zerobase.tablereservation.visitor.dto.ReserveRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreServiceImpl storeServiceImpl;

    private MemberEntity authenticate(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("로그인이 제대로 안 되어 있습니다");
        }

        return (MemberEntity) authentication.getPrincipal();
    }

    // http://localhost:8080/store/register
    @PostMapping("/register")
    @PreAuthorize("hasRole('STORE_OWNER')")
    public StoreRegister.Response registerStore(
            @RequestBody StoreRegister.Request request,
            Authentication authentication
    ) {

        MemberEntity member = authenticate(authentication);

        return storeServiceImpl.registerStore(request, member);
    }

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
    public StoreSearch.StoreDetailResponse getStore(
        @RequestParam Long storeId
    ){
        return storeServiceImpl.getStore(storeId);
    }


}
