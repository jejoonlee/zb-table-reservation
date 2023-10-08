package com.zerobase.tablereservation.visitor.dto;

import com.sun.istack.NotNull;
import lombok.*;

public class ReviewMessage {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @NotNull
        private Long reservationId;

        @NotNull
        private String review;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private Long reservationId;
        private String storeName;
        private String review;

        public static Response fromDto(ReserveDto reserveDto) {
            return Response.builder()
                    .reservationId(reserveDto.getReservationId())
                    .storeName(reserveDto.getStoreName())
                    .review(reserveDto.getReview())
                    .build();
        }
    }
}
