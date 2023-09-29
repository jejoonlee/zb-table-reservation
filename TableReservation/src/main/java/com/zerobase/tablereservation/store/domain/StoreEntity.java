package com.zerobase.tablereservation.store.domain;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name="store")
@EntityListeners(value = AuditingEntityListener.class)
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="STORE_ID", unique = true, nullable = false)
    private Long storeId;

    @ManyToOne
    @JoinColumn(name="USERNAME")
    private MemberEntity username;

    @Column(name="STORE_NAME")
    private String storeName;

    @Column(name="ADDRESS1", nullable = false)
    private String address1;

    @Column(name="ADDRESS2", nullable = false)
    private String address2;

    @Column(name="Detail", nullable = false)
    private String detail;

    @Column(name="OPEN_TIME", nullable = false)
    private String openTime;

    @Column(name="CLOSE_TIME", nullable = false)
    private String closeTime;

    @Column(name="BREAK_START", nullable = true)
    private String breakStart;

    @Column(name="BREAK_FINISH", nullable = true)
    private String breakFinish;

    @Column(name ="REGISTERED_AT", nullable = false)
    @CreatedDate
    private LocalDateTime registeredAt;

    @Column(name = "UPDATED_AT", nullable = true)
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
