package com.zerobase.tablereservation.store.controller;

import com.zerobase.tablereservation.store.dto.StoreRegister;
import com.zerobase.tablereservation.store.service.impl.StoreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.MappedSuperclass;

@RequiredArgsConstructor
@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreServiceImpl storeServiceImpl;

    @PostMapping("/register")
    public StoreRegister.Response registerStore(
            @RequestBody StoreRegister.Request request,
            Authentication authentication
    ) {
        return storeServiceImpl.registerStore(request, authentication);
    }

}
