package com.zerobase.tablereservation.store.service.impl;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.member.repository.MemberRepository;
import com.zerobase.tablereservation.store.domain.StoreEntity;
import com.zerobase.tablereservation.store.dto.StoreCancelReserve;
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

    private String CANCEL_SUCCESS_MESSAGE = "예약 내역을 성공적으로 삭제했습니다";

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
    public String cancelReservation(StoreCancelReserve.Request request, MemberEntity member) {

        // 예약이 존재하는지 확인
        ReserveEntity reserve = reserveRepository.findById(request.getReservationId())
                .orElseThrow(() -> new RuntimeException("입력한 예약이 없습니다"));

        // 예약 내역에 저장된 매장의 주인 정보와, 로그인한 주인 정보가 일치하는지 확인
        if (!reserve.getStoreEntity().getUsername().getUsername().equals(member.getUsername()))
            throw new RuntimeException("매장을 등록한 유저와, 로그인 유저가 일치하지 않습니다");

        // 예약의 시간이 이미 지났거나, 예약 사용 후거나 취소가 된 상태면 예약을 취소할 수 없다
        if (!isValidReserve(reserve))
            throw new RuntimeException("이미 취소가 되었거나, 사용이 된 예약입니다");

        // request로 받아온 매장 주인이 입력한 예약자의 이름과, 예약자의 이름이 일치하는지 확인
        // 엉뚱한 예약 내역을 삭제하면 안 되니깐, 최대한 정확히 확인을 한다
        if (!request.getUsername().equals(reserve.getMemberEntity().getUsername()))
            throw new RuntimeException("입력한 예약자의 이름과 예약 내역의 유저 이름이 일치하지 않습니다");

        reserve.setServiceUse(ServiceUseStatus.CANCEL_BY_OWNER);
        reserveRepository.save(reserve);

        return CANCEL_SUCCESS_MESSAGE;
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

    // 예약의 시간이 이미 지났거나, 예약 사용 후거나 취소가 된 상태면 예약을 취소할 수 없다
    private boolean isValidReserve(ReserveEntity reserve) {
        LocalDateTime now = LocalDateTime.now();
        now.minusMinutes(10);

        if (now.isAfter(reserve.getReserveDate())) {
            reserve.setServiceUse(ServiceUseStatus.CANCEL_TIME_LIMIT);
            reserveRepository.save(reserve);
            return false;
        }

        if (reserve.getServiceUse() != ServiceUseStatus.BEFORE) return false;

        return true;
    }
}
