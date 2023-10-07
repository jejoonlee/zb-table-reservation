package com.zerobase.tablereservation.store.dto;

import lombok.*;
import org.jetbrains.annotations.NotNull;

public class StoreCancelReserve {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotNull
        private Long reservationId;

        @NotNull
        private Long storeId;

        // 예약한 사람의 이름
        @NotNull
        private String username;

    }
}
