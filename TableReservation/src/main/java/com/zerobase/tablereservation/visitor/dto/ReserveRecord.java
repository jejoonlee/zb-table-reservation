package com.zerobase.tablereservation.visitor.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ReserveRecord {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private Long reservationNum;
        private String username;
        private String storeName;
        private String storeAddress;
        private Long peopleNum;
        private LocalDateTime reserveDate;
        private String serviceUse;

        public static Response fromDto(ReserveDto reserveDto) {
            return Response.builder()
                    .reservationNum(reserveDto.getReservationId())
                    .username(reserveDto.getUsername())
                    .storeName(reserveDto.getStoreName())
                    .storeAddress(reserveDto.getStoreAddress())
                    .peopleNum(reserveDto.getPeopleNum())
                    .reserveDate(reserveDto.getReserveDate())
                    .serviceUse(reserveDto.getServiceUse())
                    .build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseList {
        private List<ReserveDto> list;
    }
}
