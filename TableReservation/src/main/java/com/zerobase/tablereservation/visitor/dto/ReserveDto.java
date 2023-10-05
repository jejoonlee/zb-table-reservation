package com.zerobase.tablereservation.visitor.dto;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.store.domain.StoreEntity;
import com.zerobase.tablereservation.visitor.domain.ReserveEntity;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveDto {
    private Long reservationId;
    private String username;
    private Long storeId;
    private String storeName;
    private Long peopleNum;
    private LocalDateTime reserveDate;
    private String serviceUse;
    private String review;
    private LocalDateTime makeReserveAt;

    public static ReserveDto fromEntity(ReserveEntity reserveEntity) {
        MemberEntity member = reserveEntity.getMemberEntity();
        StoreEntity store = reserveEntity.getStoreEntity();

        return ReserveDto.builder()
                .reservationId(reserveEntity.getReservationId())
                .username(member.getUsername())
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .peopleNum(reserveEntity.getPeopleNum())
                .reserveDate(reserveEntity.getReserveDate())
                .review(reserveEntity.getReview())
                .makeReserveAt(reserveEntity.getMakeReserveAt())
                .build();
    }
}
