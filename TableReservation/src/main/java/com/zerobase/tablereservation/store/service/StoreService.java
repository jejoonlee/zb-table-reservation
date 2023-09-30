package com.zerobase.tablereservation.store.service;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.store.dto.StoreRegister;

public interface StoreService {
    
    StoreRegister.Response registerStore(StoreRegister.Request request, MemberEntity member);
}
