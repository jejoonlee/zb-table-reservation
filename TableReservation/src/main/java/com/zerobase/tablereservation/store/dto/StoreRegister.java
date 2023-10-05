package com.zerobase.tablereservation.store.dto;

import com.sun.istack.NotNull;
import com.zerobase.tablereservation.store.domain.StoreEntity;
import lombok.*;

public class StoreRegister {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotNull
        private String storeName;
        @NotNull
        private String address1;
        @NotNull
        private String address2;
        @NotNull
        private String detail;
        @NotNull
        private String openTime;
        @NotNull
        private String lastReserveTime;
        private String breakStart;
        private String breakFinish;

        public StoreEntity toEntity() {
            return StoreEntity.builder()
                    .storeName(this.storeName)
                    .address1(this.address1)
                    .address2(this.address2)
                    .detail(this.detail)
                    .openTime(this.openTime)
                    .lastReserveTime(this.lastReserveTime)
                    .breakStart(this.breakStart)
                    .breakEnd(this.breakFinish)
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String storeName;
        private String address1;
        private String address2;
        private String detail;
        private String openTime;
        private String lastReserveTime;


        public static StoreRegister.Response from(StoreDto storeDto) {
            return Response.builder()
                    .storeName(storeDto.getStoreName())
                    .address1(storeDto.getAddress1())
                    .address2(storeDto.getAddress2())
                    .detail(storeDto.getDetail())
                    .openTime(storeDto.getOpenTime())
                    .lastReserveTime(storeDto.getLastReserveTime())
                    .build();
        }

    }
}
