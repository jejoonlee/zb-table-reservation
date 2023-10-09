package com.zerobase.tablereservation.kiosk.service.impl;

import com.zerobase.tablereservation.exception.ErrorCode;
import com.zerobase.tablereservation.exception.KioskException;
import com.zerobase.tablereservation.exception.ReserveException;
import com.zerobase.tablereservation.kiosk.service.KisoskService;
import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.member.repository.MemberRepository;
import com.zerobase.tablereservation.visitor.domain.ReserveEntity;
import com.zerobase.tablereservation.visitor.domain.ServiceUseStatus;
import com.zerobase.tablereservation.visitor.repository.ReserveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class KioskServiceImpl implements KisoskService {

    private final ReserveRepository reserveRepository;
    private final MemberRepository memberRepository;


    private String CONFIRM_MESSAGE =
            "예약이 확인 되셨습니다\n" +
                    "즐거운 하루 되세요 :)\n" +
                    "ps.매장 이용 후 후기 작성을 부탁드립니다\n";


    private void validUser(ReserveEntity reserve, String username) {
        MemberEntity member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ReserveException(ErrorCode.USER_NOT_FOUND));

        MemberEntity reserveMember = reserve.getMemberEntity();

        if (member.getUsername() != reserveMember.getUsername()) {
            throw new ReserveException(ErrorCode.UNMATCHING_USER_AND_RESERVATION);
        }
    }

    private boolean validReserveTime(ReserveEntity reserve) {
        LocalDateTime reserveTime = reserve.getReserveDate();
        LocalDateTime now = LocalDateTime.now();

        // 현재 기준으로 10분 이후에 도착하면 예약 취소됨
        now.minusMinutes(10);

        if(now.isAfter(reserveTime)) return false;

        return true;
    }

    // ServiceUseStatus.BEFORE 가 아닌 경우
    // 서비스를 이미 사용했거나, 예약이 취소된 경우
    private boolean beforeReserve(ReserveEntity reserve) {

        if (reserve.getServiceUse() != ServiceUseStatus.BEFORE) {
            return false;
        }

        return true;
    }

    @Override
    public String checkReservation(Long reservationNum, String username) {

        ReserveEntity reserve = reserveRepository.findById(reservationNum)
                .orElseThrow(() -> new ReserveException(ErrorCode.RESERVATION_NOT_FOUND));

        validUser(reserve, username);

        // 예약한 시간이 지났을 경우,
            // ServiceUseStatus가 취소로 바뀐다
            // 예약 취소 메세지가 반환된다
        if (!validReserveTime(reserve)) {
            reserve.setServiceUse(ServiceUseStatus.CANCEL_TIME_LIMIT);
            reserveRepository.save(reserve);
            throw new KioskException(ErrorCode.CANCEL_BY_TIME_MESSAGE);
        }

        if (!beforeReserve(reserve)) {
            throw new KioskException(ErrorCode.CANCELED_RESERVE);
        }

        reserve.setServiceUse(ServiceUseStatus.USED);
        reserveRepository.save(reserve);

        return CONFIRM_MESSAGE;
    }
}
