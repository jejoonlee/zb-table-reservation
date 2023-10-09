package com.zerobase.tablereservation.member.dto;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

public class MemberRegister {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message="ID는 필수 입력 사항입니다")
        private String username;

        @NotBlank(message="비밀번호는 필수 입력 사항입니다")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\\\d)(?=.*\\\\W).{8,100}$",
                message = "영문, 특수문자, 숫자 포함 8자 이상 넣어야 합니다")
        private String password;

        @NotBlank(message="전화번호는 필수 입력 사항입니다")
        @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
                message = "010-0000-0000 형식으로 입력해주세요")
        private String phoneNumber;

        @NotBlank(message="이메일은 필수 입력 사항입니다")
        @Email(message = "이메일 형식에 맞지 않습니다")
        private String email;

        @NotBlank(message="이름은 필수 입력 사항입니다")
        private String name;

        @NotBlank(message="권한은 필수 입력 사항입니다")
        @Pattern(regexp = "0|1",
                message = "매장 점장은 0, 이용객은 1을 입력해주세요")
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
