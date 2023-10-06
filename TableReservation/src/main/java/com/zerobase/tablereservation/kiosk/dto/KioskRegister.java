package com.zerobase.tablereservation.kiosk.dto;

import lombok.*;
import org.jetbrains.annotations.NotNull;

public class KioskRegister {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        @NotNull
        private Long reservationNum;

        @NotNull
        private String username;
    }

}
