package com.zerobase.tablereservation.visitor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class ReservationMessage {


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        // 매장을 찾고, 로그인 상태에서 예약 시간을 저장하면 됨
        @Positive(message="매장 번호는 필수 입력 사항입니다. 매장 상세 정보에서 확인하실 수 있습니다")
        private Long storeId;

        @Positive(message="인원 수를 꼭 입력해주세요")
        private Long peopleNum;

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

        @Positive(message="예약 번호는 필수 입력 사항입니다. 예약 내역에 예약 번호를 확인하실 수 있습니다.")
        private Long reservationNum;

        @Positive(message="매장 번호는 필수 입력 사항입니다. 매장 상세 정보에서 확인하실 수 있습니다")
        private Long storeId;

        @Positive(message="인원 수를 꼭 입력해주세요")
        private Long peopleNum;


        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
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
