package com.zerobase.tablereservation.member.dto;

import com.zerobase.tablereservation.member.domain.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private String name;
    private List<String> role;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;

    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .phoneNumber(member.getPhoneNumber())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .registeredAt(member.getRegisteredAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
