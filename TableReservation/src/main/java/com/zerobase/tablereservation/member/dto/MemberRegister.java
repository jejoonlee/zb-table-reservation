package com.zerobase.tablereservation.member.dto;

import com.sun.istack.NotNull;
import lombok.*;

public class MemberRegister {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        @NotNull
        private String username;
        @NotNull
        private String password;
        @NotNull
        private String phoneNumber;
        @NotNull
        private String email;
        @NotNull
        private String name;
        @NotNull
        private String ownerOrCustomer;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String username;
        private String ownerOrCustomer;
        private String name;
        private String email;

        public static Response from(MemberDto memberDto) {
            return Response.builder()
                    .username(memberDto.getUsername())
                    .ownerOrCustomer(memberDto.getOwnerOrCustomer())
                    .name(memberDto.getName())
                    .email(memberDto.getEmail())
                    .build();
        }
    }
}
