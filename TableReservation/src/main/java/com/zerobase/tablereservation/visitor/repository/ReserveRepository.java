package com.zerobase.tablereservation.visitor.repository;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.visitor.domain.ReserveEntity;
import com.zerobase.tablereservation.visitor.domain.ServiceUseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReserveRepository extends JpaRepository<ReserveEntity, Long> {

    List<ReserveEntity> findAllByMemberEntityOrderByReserveDateDesc(MemberEntity member);

    List<ReserveEntity> findAllByMemberEntityAndServiceUse(MemberEntity member, ServiceUseStatus serviceUseStatus);

}
