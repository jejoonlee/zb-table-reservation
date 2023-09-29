package com.zerobase.tablereservation.store.service;

import com.zerobase.tablereservation.store.dto.StoreRegister;
import org.springframework.security.core.Authentication;

public interface StoreService {

    StoreRegister.Response registerStore(StoreRegister.Request request, Authentication authentication);
}
