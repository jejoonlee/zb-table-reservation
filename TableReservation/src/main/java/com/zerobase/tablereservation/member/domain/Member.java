package com.zerobase.tablereservation.member.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name="member")
@EntityListeners(value = AuditingEntityListener.class)
public class Member implements UserDetails {
    @Id
    @Column(name ="USERNAME", unique = true)
    private String username;

    @Column(name ="PASSWORD", nullable = false, length = 100)
    private String password;

    @Column(name ="PHONE_NUMBER", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name ="EMAIL", nullable = false, length = 50)
    private String email;

    @Column(name ="NAME", nullable = false, length = 50)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "ROLE", length = 20)
    private List<String> role;

    @Column(name ="REGISTERED_AT", nullable = false)
    @CreatedDate
    private LocalDateTime registeredAt;

    @Column(name = "UPDATED_AT", nullable = true)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.stream()
                .map(SimpleGrantedAuthority::new) // 스프링 security에서 제공하는 role 관련된 authority를 사용
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
