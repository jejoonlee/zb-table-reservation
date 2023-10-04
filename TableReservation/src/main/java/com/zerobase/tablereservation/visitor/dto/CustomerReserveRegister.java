package com.zerobase.tablereservation.visitor.dto;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class CustomerReserveRegister {


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
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private Date reserveDate;
        @NotNull
        private String reserveTime;


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
        private Date reserveDate;
        private String reserveTime;
        private String resultMessage;
        private static String MESSAGE =
                "매장에 도착했을 때에 키오스크에 예약 번호와 이름을 입력해주세요\n" +
                        "예약 10분 전까지 도착을 못 했으면, 예약은 자동으로 취소 됩니다.";

        public static Response fromDto(ReserveDto reserveDto) {
            return Response.builder()
                    .reservationId(reserveDto.getReservationId())
                    .username(reserveDto.getUsername())
                    .storeName(reserveDto.getStoreName())
                    .reserveDate(reserveDto.getReserveDate())
                    .reserveTime(reserveDto.getReserveTime())
                    .resultMessage(MESSAGE)
                    .build();
        }
    }
}
