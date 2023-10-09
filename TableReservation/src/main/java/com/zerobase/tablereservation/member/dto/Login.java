package com.zerobase.tablereservation.member.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class Login {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message="ID는 필수 입력 사항입니다")
        private String username;
        @NotBlank(message="비밀번호는 필수 입력 사항입니다")
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
