package com.zerobase.tablereservation.member.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name="member")
@EntityListeners(value = AuditingEntityListener.class)
public class Member implements MemberCode {
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

    // 점장 또는 고객
    @Column(name ="OWNER_OR_CUSTOMER", nullable = false, length = 10)
    private String ownerOrCustomer;

    @Column(name ="REGISTERED_AT", nullable = false)
    @CreatedDate
    private LocalDateTime registeredAt;

    @Column(name = "UPDATED_AT", nullable = true)
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
