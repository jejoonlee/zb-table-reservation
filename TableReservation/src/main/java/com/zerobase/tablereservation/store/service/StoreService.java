package com.zerobase.tablereservation.store.service;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.store.dto.StoreCancelReserve;
import com.zerobase.tablereservation.store.dto.StoreMessage;
import com.zerobase.tablereservation.store.dto.StoreDetailMessage;
import com.zerobase.tablereservation.visitor.dto.ReserveRecord;

import java.util.HashMap;
import java.util.List;

public interface StoreService {

    StoreMessage.Response registerStore(StoreMessage.Request request, MemberEntity member);

    List<StoreMessage.Response> getAllRegisteredStores(MemberEntity member);

    StoreMessage.Response updateStore(StoreMessage.UpdateRequest request, MemberEntity member);

    List<ReserveRecord.Response> getAllReservations(Long storeId, MemberEntity member);

    String cancelReservation(StoreCancelReserve.Request request, MemberEntity member);

    StoreDetailMessage.Response getStore(Long storeId);

    List<HashMap<String, String>> getAllStores(String keyword);
}
