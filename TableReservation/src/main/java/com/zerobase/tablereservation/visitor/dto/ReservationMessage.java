package com.zerobase.tablereservation.visitor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ReservationMessage {


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        // 매장을 찾고, 로그인 상태에서 예약 시간을 저장하면 됨
        @NotNull
        private Long storeId;
        @NotNull
        private Long peopleNum;

        @NotNull
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime reserveDate;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestUpdate {

        @NotNull
        private Long reservationNum;

        @NotNull
        private Long storeId;

        @NotNull
        private Long peopleNum;

        @NotNull
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime reserveDate;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private Long reservationId;
        private String username;
        private String storeName;
        private String storeAddress;
        private LocalDateTime reserveDate;
        private String resultMessage;
        private static String MESSAGE =
                "매장에 도착했을 때에 키오스크에 예약 번호와 이름을 입력해주세요\n" +
                        "예약 10분 전까지 도착을 못 했으면, 예약은 자동으로 취소 됩니다.";

        public static Response fromDto(ReserveDto reserveDto) {
            return Response.builder()
                    .reservationId(reserveDto.getReservationId())
                    .username(reserveDto.getUsername())
                    .storeName(reserveDto.getStoreName())
                    .storeAddress(reserveDto.getStoreAddress())
                    .reserveDate(reserveDto.getReserveDate())
                    .resultMessage(MESSAGE)
                    .build();
        }
    }
}
