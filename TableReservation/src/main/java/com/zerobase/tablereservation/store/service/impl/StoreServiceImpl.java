package com.zerobase.tablereservation.store.service.impl;

import com.zerobase.tablereservation.exception.ErrorCode;
import com.zerobase.tablereservation.exception.ReserveException;
import com.zerobase.tablereservation.exception.StoreException;
import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.member.repository.MemberRepository;
import com.zerobase.tablereservation.store.domain.StoreEntity;
import com.zerobase.tablereservation.store.dto.StoreCancelReserve;
import com.zerobase.tablereservation.store.dto.StoreDto;
import com.zerobase.tablereservation.store.dto.StoreMessage;
import com.zerobase.tablereservation.store.dto.StoreDetailMessage;
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
    private String DELETE_STORE_SUCCESS = "매장을 성공적으로 삭제했습니다";

    @Override
    public StoreMessage.Response registerStore(StoreMessage.Request request, MemberEntity member) {

        StoreEntity storeEntity = request.toEntity();

        String breakStart;
        String breakFinish;

        if (request.getBreakStart().equals("없음")) {
            breakStart = null;
        } else {
            breakStart = request.getBreakStart();
        }
        if (request.getBreakFinish().equals("없음")) {
            breakFinish = null;
        } else {
            breakFinish = request.getBreakFinish();
        }

        storeEntity.setBreakStart(breakStart);
        storeEntity.setBreakEnd(breakFinish);
        storeEntity.setUsername(member);
        storeEntity.setRegisteredAt(LocalDateTime.now());

        storeRepository.save(storeEntity);

        StoreDto storeDto = StoreDto.fromEntity(storeEntity);

        return StoreMessage.Response.fromDto(storeDto);
    }

    @Override
    public List<StoreMessage.Response> getAllRegisteredStores(MemberEntity member) {

        List<StoreMessage.Response> result = new ArrayList<>();
        List<StoreEntity> allStores = storeRepository.findAllByUsername(member);

        for (StoreEntity store : allStores) {
            result.add(StoreMessage.Response.fromDto(StoreDto.fromEntity(store)));
        }

        return result;
    }

    @Override
    public StoreMessage.Response updateStore(StoreMessage.UpdateRequest request, MemberEntity member) {

        StoreEntity store = storeRepository.findByStoreId(request.getStoreNum())
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));

        if (!isValidOwner(store, member))
            throw new StoreException(ErrorCode.UNMATCHING_USER_AND_STORE_OWNER);

        String breakStart;
        String breakFinish;

        if (request.getBreakStart().equals("없음")) {
            breakStart = null;
        } else {
            breakStart = request.getBreakStart();
        }
        if (request.getBreakFinish().equals("없음")) {
            breakFinish = null;
        } else {
            breakFinish = request.getBreakFinish();
        }

        store.setStoreName(request.getStoreName());
        store.setAddress1(request.getAddress1());
        store.setAddress2(request.getAddress2());
        store.setDetail(request.getDetail());
        store.setOpenTime(request.getOpenTime());
        store.setLastReserveTime(request.getLastReserveTime());
        store.setBreakStart(breakStart);
        store.setBreakEnd(breakFinish);
        store.setUpdatedAt(LocalDateTime.now());

        storeRepository.save(store);

        return StoreMessage.Response.fromDto(StoreDto.fromEntity(store));
    }

    @Override
    public String deleteStore(Long storeNum, MemberEntity member) {

        StoreEntity store = storeRepository.findByStoreId(storeNum)
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));

        if (!isValidOwner(store, member))
            throw new StoreException(ErrorCode.UNMATCHING_USER_AND_STORE_OWNER);

        // 해당 매장에 대한 모든 예약들을 삭제하고난 후에, 매장을 삭제한다
        reserveRepository.deleteAllByStoreEntity(store);
        storeRepository.delete(store);

        return DELETE_STORE_SUCCESS;
    }


    @Override
    public List<ReserveRecord.Response> getAllReservations(Long storeId, MemberEntity member) {

        StoreEntity store = storeRepository.findByStoreId(storeId)
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));

        // 입력한 상점이, 로그인한 주인이 등록한 상점인지 확인하기
        if (!isValidOwner(store, member)) throw new StoreException(ErrorCode.UNMATCHING_USER_AND_STORE_OWNER);


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
                .orElseThrow(() -> new ReserveException(ErrorCode.RESERVATION_NOT_FOUND));

        // 예약 내역에 저장된 매장의 주인 정보와, 로그인한 주인 정보가 일치하는지 확인
        if (!isValidOwner(reserve.getStoreEntity(), member)) throw new StoreException(ErrorCode.UNMATCHING_USER_AND_STORE_OWNER);

        // 예약의 시간이 이미 지났거나, 예약 사용 후거나 취소가 된 상태면 예약을 취소할 수 없다
        if (!isValidReserve(reserve))
            throw new ReserveException(ErrorCode.CANCELED_OR_ALREADY_USED_RESERVATION);

        // request로 받아온 매장 주인이 입력한 예약자의 이름과, 예약자의 이름이 일치하는지 확인
        // 엉뚱한 예약 내역을 삭제하면 안 되니깐, 최대한 정확히 확인을 한다
        if (!request.getUsername().equals(reserve.getMemberEntity().getUsername()))
            throw new ReserveException(ErrorCode.UNMATCHING_REQUESTED_USER_AND_RESERVATION);

        reserve.setServiceUse(ServiceUseStatus.CANCEL_BY_OWNER);
        reserveRepository.save(reserve);

        return CANCEL_SUCCESS_MESSAGE;
    }

    @Override
    public StoreDetailMessage.Response getStore(Long storeId) {

        StoreEntity store = storeRepository.findByStoreId(storeId)
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));

        StoreDto storeDto = StoreDto.fromEntity(store);

        return StoreDetailMessage.Response.from(storeDto);
    }

    @Override
    public List<HashMap<String, String>> getAllStores(String keyword) {

        List<StoreEntity> stores = storeRepository
                .findAllByStoreNameStartingWithIgnoreCase(keyword);

        if (stores.size() == 0) throw new StoreException(ErrorCode.KEYWORD_NOT_FOUND);

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
