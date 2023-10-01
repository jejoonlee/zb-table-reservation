package com.zerobase.tablereservation.store.service;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.store.dto.StoreRegister;
import com.zerobase.tablereservation.store.dto.StoreSearch;

public interface StoreService {

    StoreRegister.Response registerStore(StoreRegister.Request request, MemberEntity member);

    StoreSearch.StoreDetailResponse getStore(Long storeId);
}
