package com.zerobase.tablereservation.visitor.domain;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.store.domain.StoreEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="reservation")
@Builder
@EntityListeners(value = AuditingEntityListener.class)
public class ReserveEntity {

    @Id
    @Column(name="RESERVATION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name="USERNAME")
    private MemberEntity memberEntity;

    @ManyToOne
    @JoinColumn(name="STORE_ID")
    private StoreEntity storeEntity;

    @Column(name="PEOPLE_NUM")
    private Long peopleNum;

    @Column(name = "RESERVE_DATE", nullable = false)
    private LocalDateTime reserveDate;

    @Column(name = "SERVICE_USE", nullable = false)
    private ServiceUseStatus serviceUse;

    @Column(name = "REVIEW")
    private String review;

    @Column(name ="MAKE_RESERVE_AT", nullable = false)
    @CreatedDate
    private LocalDateTime makeReserveAt;

}
