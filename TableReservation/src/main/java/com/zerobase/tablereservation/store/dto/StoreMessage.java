package com.zerobase.tablereservation.store.dto;

import com.zerobase.tablereservation.store.domain.StoreEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class StoreMessage {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotBlank(message="매장 이름은 필수 입력 사항입니다")
        private String storeName;

        @NotBlank(message="매장 주소는 필수 입력 사항입니다")
        private String address1;

        @NotBlank(message="매장 상세 주소는 필수 입력 사항입니다")
        private String address2;

        @NotBlank(message="매장 설명은 필수 입력 사항입니다")
        private String detail;

        @NotBlank(message="매장 오픈 시간은 필수 입력 사항입니다")
        @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):(0[1-9]|[0-5][0-9])$",
                message = "13:00 형태로 작성해주세요. ':' 와 24시 형태는 필수입니다")
        private String openTime;

        @NotBlank(message="매장 마지막 예약 시간은 필수 입력 사항입니다")
        @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):(0[1-9]|[0-5][0-9])$",
                message = "13:00 형태로 작성해주세요. ':' 와 24시 형태는 필수입니다")
        private String lastReserveTime;

        @Pattern(regexp = "없음|^(0[0-9]|1[0-9]|2[0-3]):(0[1-9]|[0-5][0-9])$?",
                message = "'없음' 또는 13:00 형태로 작성해주세요. ':' 와 24시 형태는 필수입니다")
        private String breakStart;

        @Pattern(regexp = "없음|^(0[0-9]|1[0-9]|2[0-3]):(0[1-9]|[0-5][0-9])$?",
                message = "'없음' 또는 13:00 형태로 작성해주세요. ':' 와 24시 형태는 필수입니다")
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
    public static class UpdateRequest {
        @NotBlank(message="매장 번호는 필수 입력 사항입니다. 매장 상세 정보에서 확인하실 수 있습니다")
        private Long storeNum;

        @NotBlank(message="매장 이름은 필수 입력 사항입니다")
        private String storeName;

        @NotBlank(message="매장 주소는 필수 입력 사항입니다")
        private String address1;

        @NotBlank(message="매장 상세 주소는 필수 입력 사항입니다")
        private String address2;

        @NotBlank(message="매장 설명은 필수 입력 사항입니다")
        private String detail;

        @NotBlank(message="매장 오픈 시간는 필수 입력 사항입니다")
        @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):(0[1-9]|[0-5][0-9])$",
                message = "13:00 형태로 작성해주세요. ':' 와 24시 형태는 필수입니다")
        private String openTime;

        @NotBlank(message="매장 마지막 예약 시간은 필수 입력 사항입니다")
        @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):(0[1-9]|[0-5][0-9])$",
                message = "13:00 형태로 작성해주세요. ':' 와 24시 형태는 필수입니다")
        private String lastReserveTime;

        @Pattern(regexp = "없음|^(0[0-9]|1[0-9]|2[0-3]):(0[1-9]|[0-5][0-9])$?",
                message = "'없음' 또는 13:00 형태로 작성해주세요. ':' 와 24시 형태는 필수입니다")
        private String breakStart;

        @Pattern(regexp = "없음|^(0[0-9]|1[0-9]|2[0-3]):(0[1-9]|[0-5][0-9])$?",
                message = "'없음' 또는 13:00 형태로 작성해주세요. ':' 와 24시 형태는 필수입니다")
        private String breakFinish;

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
        private String breakTime;


        public static StoreMessage.Response fromDto(StoreDto storeDto) {
            String breakTime;

            if (storeDto.getBreakStart() == null) {
                breakTime = "없음";
            } else {
                breakTime = storeDto.getBreakStart() + " ~ " + storeDto.getBreakFinish();
            }

            return Response.builder()
                    .storeName(storeDto.getStoreName())
                    .address1(storeDto.getAddress1())
                    .address2(storeDto.getAddress2())
                    .detail(storeDto.getDetail())
                    .openTime(storeDto.getOpenTime())
                    .lastReserveTime(storeDto.getLastReserveTime())
                    .breakTime(breakTime)
                    .build();
        }

    }
}
