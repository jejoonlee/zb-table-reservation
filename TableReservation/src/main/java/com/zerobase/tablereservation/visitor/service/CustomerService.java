package com.zerobase.tablereservation.visitor.service;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.visitor.dto.ReservationMessage;
import com.zerobase.tablereservation.visitor.dto.ReserveRecord;
import com.zerobase.tablereservation.visitor.dto.ReviewMessage;

import java.util.List;

public interface CustomerService {

    ReservationMessage.Response makeReservation(ReservationMessage.Request request, MemberEntity member);
    List<ReserveRecord.Response> getMyReservations(MemberEntity member);

    ReservationMessage.Response updateReservation(ReservationMessage.RequestUpdate request, MemberEntity member);

    String cancelReservation(Long reserveNum, MemberEntity member);

    ReviewMessage.Response writeReview(ReviewMessage.Request request, MemberEntity member);

    List<ReviewMessage.Response> getAllUserReview(MemberEntity member);

    ReviewMessage.Response updateReview(ReviewMessage.Request request, MemberEntity member);

}
