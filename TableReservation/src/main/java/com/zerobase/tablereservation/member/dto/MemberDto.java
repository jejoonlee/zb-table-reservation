package com.zerobase.tablereservation.member.dto;

import com.zerobase.tablereservation.member.domain.MemberEntity;
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

    public static MemberDto fromEntity(MemberEntity memberEntity) {
        return MemberDto.builder()
                .username(memberEntity.getUsername())
                .password(memberEntity.getPassword())
                .phoneNumber(memberEntity.getPhoneNumber())
                .email(memberEntity.getEmail())
                .name(memberEntity.getName())
                .role(memberEntity.getRole())
                .registeredAt(memberEntity.getRegisteredAt())
                .updatedAt(memberEntity.getUpdatedAt())
                .build();
    }
}
