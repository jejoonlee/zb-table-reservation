package com.zerobase.tablereservation.store.service.impl;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.member.repository.MemberRepository;
import com.zerobase.tablereservation.store.domain.StoreEntity;
import com.zerobase.tablereservation.store.dto.StoreDto;
import com.zerobase.tablereservation.store.dto.StoreRegister;
import com.zerobase.tablereservation.store.dto.StoreSearch;
import com.zerobase.tablereservation.store.repository.StoreRepository;
import com.zerobase.tablereservation.store.service.StoreService;
import com.zerobase.tablereservation.visitor.domain.ReserveEntity;
import com.zerobase.tablereservation.visitor.domain.ServiceUseStatus;
import com.zerobase.tablereservation.visitor.dto.ReserveDto;
import com.zerobase.tablereservation.visitor.dto.ReserveRecord;
import com.zerobase.tablereservation.visitor.repository.ReserveRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final ReserveRepository reserveRepository;

    @Override
    public StoreRegister.Response registerStore(StoreRegister.Request request, MemberEntity member) {

        StoreEntity storeEntity = request.toEntity();

        storeEntity.setUsername(member);
        storeEntity.setRegisteredAt(LocalDateTime.now());

        storeRepository.save(storeEntity);

        StoreDto storeDto = StoreDto.fromEntity(storeEntity);

        return StoreRegister.Response.from(storeDto);
    }


    @Override
    public List<ReserveRecord.Response> getAllReservations(Long storeId, MemberEntity member) {

        StoreEntity store = storeRepository.findByStoreId(storeId)
                .orElseThrow(() -> new RuntimeException("등록되지 않은 매장 ID 입니다"));

        // 입력한 상점이, 로그인한 주인이 등록한 상점인지 확인하기
        if (!isValidOwner(store, member)) throw new RuntimeException("등록한 매장에 대한 예약 내역만 볼 수 있습니다");


        // 맞으면 해당 상점에 대한 모든 예약 내역 받아오기 (오름차순 + 예약 상태 Before만)
        List<ReserveEntity> reserveEntityList = reserveRepository
                .findAllByStoreEntityAndServiceUseOrderByReserveDateDesc(store, ServiceUseStatus.BEFORE);

        // Before 상태인 예약들을 확인하면서, 예약 진행이 가능한 내역만 출력한다
        // 이미 지난 상태이면 자동으로 취소를 시킨다
        return checkReservationStatus(reserveEntityList);
    }

    @Override
    public StoreSearch.StoreDetailResponse getStore(Long storeId) {

        StoreEntity store = storeRepository.findByStoreId(storeId)
                .orElseThrow(() -> new RuntimeException("등록되지 않은 매장 ID 입니다"));

        StoreDto storeDto = StoreDto.fromEntity(store);

        return StoreSearch.StoreDetailResponse.from(storeDto);
    }

    @Override
    public List<HashMap<String, String>> getAllStores(String keyword) {

        List<StoreEntity> stores = storeRepository
                .findAllByStoreNameStartingWithIgnoreCase(keyword);

        if (stores.size() == 0) throw new RuntimeException("검색한 키워드에 대한 정보가 없습니다");

        List<HashMap<String, String>> result = new ArrayList<>();

        for (StoreEntity store : stores) {
            HashMap<String, String> map = new HashMap<>();
            map.put("매장 ID", String.format("%d", store.getStoreId()));
            map.put("매장 이름", store.getStoreName());
            map.put("매장 주소", store.getAddress1() + store.getAddress2());

            result.add(map);
        }

        return result;
    }

    private List<ReserveRecord.Response> checkReservationStatus(List<ReserveEntity> reserveEntityList) {
        List<ReserveRecord.Response> result = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        now.minusMinutes(10);

        for (ReserveEntity reserve : reserveEntityList) {
            if(now.isAfter(reserve.getReserveDate())) {
                reserve.setServiceUse(ServiceUseStatus.CANCEL_TIME_LIMIT);
                reserveRepository.save(reserve);
            } else {
                result.add(ReserveRecord.Response.fromDto(ReserveDto.fromEntity(reserve)));
            }
        }

        return result;
    }

    private boolean isValidOwner(StoreEntity store, MemberEntity member) {

        System.out.println(store.getUsername().getUsername() + " " + member.getUsername());
        if (!store.getUsername().getUsername().equals(member.getUsername())) return false;

        return true;
    }
}
