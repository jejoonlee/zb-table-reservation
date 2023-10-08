package com.zerobase.tablereservation.store.dto;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import lombok.*;

public class StoreDetailMessage {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private String storeName;
        private String ownerName;
        private String address1;
        private String address2;
        private String detail;
        private String openTime;
        private String lastReserveTime;
        private String breakStart;
        private String breakFinish;

        public static Response from(StoreDto storeDto) {

            MemberEntity member = storeDto.getUsername();
            String owner = member.getUsername();

            return Response.builder()
                    .storeName(storeDto.getStoreName())
                    .ownerName(owner)
                    .address1(storeDto.getAddress1())
                    .address2(storeDto.getAddress2())
                    .detail(storeDto.getDetail())
                    .openTime(storeDto.getOpenTime())
                    .lastReserveTime(storeDto.getLastReserveTime())
                    .breakStart(storeDto.getBreakStart())
                    .breakFinish(storeDto.getBreakFinish())
                    .build();
        }

    }

}
