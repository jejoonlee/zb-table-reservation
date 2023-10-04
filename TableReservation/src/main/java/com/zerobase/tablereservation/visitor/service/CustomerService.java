package com.zerobase.tablereservation.visitor.service;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.visitor.dto.CustomerReserveRegister;

public interface CustomerService {

    CustomerReserveRegister.Response makeReservation(CustomerReserveRegister.Request request, MemberEntity member);

}
