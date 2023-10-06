package com.zerobase.tablereservation.visitor.service;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.visitor.dto.CustomerReserveRegister;
import com.zerobase.tablereservation.visitor.dto.ReserveRecord;

import java.util.List;

public interface CustomerService {

    CustomerReserveRegister.Response makeReservation(CustomerReserveRegister.Request request, MemberEntity member);

    List<ReserveRecord.Response> getMyReservations(MemberEntity member);

}
