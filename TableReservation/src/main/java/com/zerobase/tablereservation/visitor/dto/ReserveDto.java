package com.zerobase.tablereservation.visitor.dto;

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
    private String storeAddress;
    private Long peopleNum;
    private LocalDateTime reserveDate;
    private String serviceUse;
    private String review;
    private LocalDateTime makeReserveAt;

    public static ReserveDto fromEntity(ReserveEntity reserveEntity) {
        StoreEntity store = reserveEntity.getStoreEntity();

        return ReserveDto.builder()
                .reservationId(reserveEntity.getReservationId())
                .username(reserveEntity.getMemberEntity().getUsername())
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .storeAddress(store.getAddress1() + " " + store.getAddress2())
                .peopleNum(reserveEntity.getPeopleNum())
                .reserveDate(reserveEntity.getReserveDate())
                .serviceUse(reserveEntity.getServiceUse().name())
                .review(reserveEntity.getReview())
                .makeReserveAt(reserveEntity.getMakeReserveAt())
                .build();
    }
}
