package com.zerobase.tablereservation.visitor.service.impl;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.store.domain.ServiceUseStatus;
import com.zerobase.tablereservation.store.domain.StoreEntity;
import com.zerobase.tablereservation.store.repository.StoreRepository;
import com.zerobase.tablereservation.visitor.domain.ReserveEntity;
import com.zerobase.tablereservation.visitor.dto.CustomerReserveRegister;
import com.zerobase.tablereservation.visitor.dto.ReserveDto;
import com.zerobase.tablereservation.visitor.repository.ReserveRepository;
import com.zerobase.tablereservation.visitor.service.CustomerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final StoreRepository storeRepository;
    private final ReserveRepository reserveRepository;

    private boolean validDate(Date reserveDate) {
        LocalDate now = LocalDate.now();

        if (reserveDate.before(now)) return false;

        return true;
    }

    private boolean validTime() {
        return true;
    }

    @Override
    public CustomerReserveRegister.Response makeReservation(CustomerReserveRegister.Request request, MemberEntity member) {

        StoreEntity store = storeRepository.findByStoreId(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("없는 상점입니다"));

        // 해당 상점이 여는 시간에 예약을 했는지 확인한다
        Date reserveDate = request.getReserveDate();
        String reserveTime = request.getReserveTime();

        System.out.println(request.getReserveDate());
        System.out.println(reserveDate);

        ReserveEntity reserve = ReserveEntity.builder()
                .memberEntity(member)
                .storeEntity(store)
                .peopleNum(request.getPeopleNum())
                .reserveDate(reserveDate)
                .reserveTime(reserveTime)
                .serviceUse(ServiceUseStatus.BEFORE)
                .review(null)
                .makeReserveAt(LocalDateTime.now())
                .build();

        reserveRepository.save(reserve);

        ReserveDto reserveDto = ReserveDto.fromEntity(reserve);

        return CustomerReserveRegister.Response.fromDto(reserveDto);
    }
}
