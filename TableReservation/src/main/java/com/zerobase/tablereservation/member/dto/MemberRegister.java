package com.zerobase.tablereservation.member.dto;

import com.sun.istack.NotNull;
import com.zerobase.tablereservation.member.domain.MemberEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
        private String role; // 문자열로 받고, service에서 리스트에 넣기

        public MemberEntity toEntity() {
            return MemberEntity.builder()
                    .username(this.username)
                    .password(this.password)
                    .phoneNumber(this.phoneNumber)
                    .email(this.email)
                    .name(this.name)
                    .registeredAt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String username;
        private List<String> role;
        private String name;
        private String email;

        public static Response from(MemberDto memberDto) {
            return Response.builder()
                    .username(memberDto.getUsername())
                    .role(memberDto.getRole())
                    .name(memberDto.getName())
                    .email(memberDto.getEmail())
                    .build();
        }
    }
}
