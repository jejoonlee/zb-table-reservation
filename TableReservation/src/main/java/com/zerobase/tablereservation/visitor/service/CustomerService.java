package com.zerobase.tablereservation.visitor.service;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.visitor.dto.CustomerReserveRegister;
import com.zerobase.tablereservation.visitor.dto.ReserveRecord;
import com.zerobase.tablereservation.visitor.dto.WriteReview;

import java.util.List;

public interface CustomerService {

    CustomerReserveRegister.Response makeReservation(CustomerReserveRegister.Request request, MemberEntity member);
    List<ReserveRecord.Response> getMyReservations(MemberEntity member);

    CustomerReserveRegister.Response updateReservation(CustomerReserveRegister.RequestUpdate request, MemberEntity member);

    WriteReview.Response writeReview(WriteReview.Request request, MemberEntity member);

}
