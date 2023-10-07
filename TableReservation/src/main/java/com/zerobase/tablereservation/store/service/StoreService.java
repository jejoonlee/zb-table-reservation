package com.zerobase.tablereservation.store.service;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.store.dto.StoreCancelReserve;
import com.zerobase.tablereservation.store.dto.StoreRegister;
import com.zerobase.tablereservation.store.dto.StoreSearch;
import com.zerobase.tablereservation.visitor.dto.ReserveRecord;

import java.util.HashMap;
import java.util.List;

public interface StoreService {

    StoreRegister.Response registerStore(StoreRegister.Request request, MemberEntity member);

    List<ReserveRecord.Response> getAllReservations(Long storeId, MemberEntity member);

    String cancelReservation(StoreCancelReserve.Request request, MemberEntity member);

    StoreSearch.StoreDetailResponse getStore(Long storeId);

    List<HashMap<String, String>> getAllStores(String keyword);
}
