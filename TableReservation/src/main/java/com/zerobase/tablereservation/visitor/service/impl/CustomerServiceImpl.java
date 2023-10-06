package com.zerobase.tablereservation.visitor.service.impl;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.member.repository.MemberRepository;
import com.zerobase.tablereservation.store.domain.StoreEntity;
import com.zerobase.tablereservation.store.repository.StoreRepository;
import com.zerobase.tablereservation.visitor.domain.ReserveEntity;
import com.zerobase.tablereservation.visitor.domain.ServiceUseStatus;
import com.zerobase.tablereservation.visitor.dto.CustomerReserveRegister;
import com.zerobase.tablereservation.visitor.dto.ReserveDto;
import com.zerobase.tablereservation.visitor.dto.ReserveRecord;
import com.zerobase.tablereservation.visitor.repository.ReserveRepository;
import com.zerobase.tablereservation.visitor.service.CustomerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final StoreRepository storeRepository;
    private final ReserveRepository reserveRepository;
    private final MemberRepository memberRepository;

    @Override
    public CustomerReserveRegister.Response makeReservation(CustomerReserveRegister.Request request, MemberEntity member) {

        StoreEntity store = storeRepository.findByStoreId(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("없는 상점입니다"));

        // 해당 상점이 여는 시간에 예약을 했는지 확인한다
        LocalDateTime reserveDate = request.getReserveDate();

        if (!validTime(reserveDate, store.getOpenTime(), store.getLastReserveTime(), store.getBreakStart(), store.getBreakEnd())) {
            throw new RuntimeException("예약을 할 수 없는 시간입니다");
        } else {
            System.out.println(reserveDate + "날짜 저장");
        }

        ReserveEntity reserve = ReserveEntity.builder()
                .memberEntity(member)
                .storeEntity(store)
                .peopleNum(request.getPeopleNum())
                .reserveDate(reserveDate)
                .serviceUse(ServiceUseStatus.BEFORE)
                .review(null)
                .makeReserveAt(LocalDateTime.now())
                .build();

        reserveRepository.save(reserve);

        ReserveDto reserveDto = ReserveDto.fromEntity(reserve);

        return CustomerReserveRegister.Response.fromDto(reserveDto);
    }

    @Override
    public List<ReserveRecord.Response> getMyReservations(MemberEntity member) {

        // 예약상태 확인 (Before을 확인해서, status를 바꿀 것이 있는지 확인 및 변경)
        List<ReserveEntity> reserve = reserveRepository.findAllByMemberEntityAndServiceUse(member, ServiceUseStatus.BEFORE);

        checkReservationStatus(reserve);

        // 최종적으로 해당 유저의 모든 정보를 리스트에 넣어서 응답하기
        return getReservationList(member);
    }

    private boolean validTime(LocalDateTime reserveDate, String openTime, String lastReserve, String breakStart, String breakEnd) {

        // 예약은 지금 시간부터 2시간 이후 부터 할 수 있다
        if (reserveDate.isBefore(LocalDateTime.now().plusHours(2))) return false;

        String[] open = openTime.split(":");
        String[] last = lastReserve.split(":");

        int openMin = Integer.parseInt(open[0]) * 60 + Integer.parseInt(open[1]);
        int lastMin = Integer.parseInt(last[0]) * 60 + Integer.parseInt(last[1]);

        int hour = reserveDate.getHour();
        int minute = reserveDate.getMinute();
        int totalMin = hour * 60 + minute;


        if (breakStart.equals("없음")) {
            // 쉬는 시간이 없는 매장일 때에
            // 예약 시간이 오픈 전 또는 예약 시간 이후 일 때에 false를 반환한다
            if (openMin > totalMin || totalMin > lastMin) return false;

        } else {
            String[] breakS = breakStart.split(":");
            String[] breakE = breakEnd.split(":");

            int breakSMin = Integer.parseInt(breakS[0]) * 60 + Integer.parseInt(breakS[1]);
            int breakEMin = Integer.parseInt(breakE[0]) * 60 + Integer.parseInt(breakE[0]);

            // 쉬는 시간이 있는 매장일 때에
            // 예약 시간이 오픈 전 또는 예약 시간 이후 일 때
            if (openMin > totalMin || totalMin > lastMin) return false;

            // 그리고 예약 시간이 쉬는 시간 시작 1시간 이후 또는 쉬는 시간 중일 때에 false를 반환한다
            if (breakSMin - 60 < totalMin && totalMin < breakEMin) return false;

        }

        return true;
    }


    private void checkReservationStatus(List<ReserveEntity> reserveBeforeUse) {
        LocalDateTime now = LocalDateTime.now();
        now.minusMinutes(10);

        for (ReserveEntity reserve : reserveBeforeUse) {
            // 예약 시간이 이미 지났거나, 예약 10분전에 유저가 kiosk에서 예약을 확인 못 하면 자동으로 CANCEL_TIME_LIMIT으로 바꾼다
            if(now.isAfter(reserve.getReserveDate())) {
                reserve.setServiceUse(ServiceUseStatus.CANCEL_TIME_LIMIT);
                reserveRepository.save(reserve);
            }
        }
    }

    private List<ReserveRecord.Response> getReservationList(MemberEntity member){
        List<ReserveEntity> list = reserveRepository.findAllByMemberEntityOrderByReserveDateDesc(member);
        List<ReserveRecord.Response> result = new ArrayList<>();

        for (ReserveEntity reserve : list) {
            result.add(ReserveRecord.Response.fromDto(ReserveDto.fromEntity(reserve)));
        }

        return result;
    }

}
