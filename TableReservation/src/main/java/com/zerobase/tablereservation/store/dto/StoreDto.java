package com.zerobase.tablereservation.store.dto;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.store.domain.StoreEntity;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDto {
    private Long storeId;
    private MemberEntity username;
    private String storeName;
    private String address1;
    private String address2;
    private String detail;
    private String openTime;
    private String closeTime;
    private String breakStart;
    private String breakFinish;
    private LocalDateTime registeredAt;

    public static StoreDto fromEntity(StoreEntity storeEntity) {
        return StoreDto.builder()
                .storeId(storeEntity.getStoreId())
                .username(storeEntity.getUsername())
                .storeName(storeEntity.getStoreName())
                .address1(storeEntity.getAddress1())
                .address2(storeEntity.getAddress2())
                .detail(storeEntity.getDetail())
                .openTime(storeEntity.getOpenTime())
                .closeTime(storeEntity.getCloseTime())
                .breakStart(storeEntity.getBreakStart())
                .breakFinish(storeEntity.getBreakEnd())
                .registeredAt(storeEntity.getRegisteredAt())
                .build();
    }

}
