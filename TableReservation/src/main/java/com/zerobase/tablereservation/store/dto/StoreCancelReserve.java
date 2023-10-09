package com.zerobase.tablereservation.store.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

public class StoreCancelReserve {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotBlank(message="예약 번호는 필수 입력 사항입니다. 매장 예약 내역에서 확인하실 수 있습니다")
        private Long reservationId;

        @NotBlank(message="매장 번호는 필수 입력 사항입니다. 매장 상세 정보에서 매장 번호를 확인하실 수 있습니다")
        private Long storeId;

        // 예약한 사람의 이름
        @NotBlank(message="예약한 사람의 ID는 필수 입력 사항입니다")
        private String username;

    }
}
