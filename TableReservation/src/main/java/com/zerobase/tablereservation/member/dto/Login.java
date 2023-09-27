package com.zerobase.tablereservation.member.dto;

import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

public class Login {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        @NotNull
        private String username;
        @NotNull
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String username;
        private List<String> ownerOrCustomer;
        private String name;

        public static Login.Response from(MemberDto memberDto) {
            return Response.builder()
                    .username(memberDto.getUsername())
                    .ownerOrCustomer(memberDto.getRole())
                    .name(memberDto.getName())
                    .build();
        }
    }
}
