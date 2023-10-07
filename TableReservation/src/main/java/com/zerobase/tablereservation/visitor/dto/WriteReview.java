package com.zerobase.tablereservation.visitor.dto;

import com.sun.istack.NotNull;
import lombok.*;

public class WriteReview {

    private static String MESSAGE = "해당 예약에 대한 리뷰를 성공적으로 작성하셨습니다";

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
        private String review;
        private String message;

        public static Response fromDto(ReserveDto reserveDto) {
            return Response.builder()
                    .reservationId(reserveDto.getReservationId())
                    .review(reserveDto.getReview())
                    .message(MESSAGE)
                    .build();
        }
    }
}
