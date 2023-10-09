package com.zerobase.tablereservation.visitor.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

public class ReviewMessage {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @NotBlank(message="예약 번호는 필수 입력 사항입니다. 예약 내역에 예약 번호를 확인하실 수 있습니다.")
        private Long reservationId;

        @NotBlank(message="리뷰를 입력해주셔야 합니다")
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
