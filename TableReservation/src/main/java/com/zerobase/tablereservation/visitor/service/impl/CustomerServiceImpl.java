package com.zerobase.tablereservation.visitor.service.impl;

import com.zerobase.tablereservation.exception.ErrorCode;
import com.zerobase.tablereservation.exception.ReserveException;
import com.zerobase.tablereservation.exception.StoreException;
import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.member.repository.MemberRepository;
import com.zerobase.tablereservation.store.domain.StoreEntity;
import com.zerobase.tablereservation.store.repository.StoreRepository;
import com.zerobase.tablereservation.visitor.domain.ReserveEntity;
import com.zerobase.tablereservation.visitor.domain.ServiceUseStatus;
import com.zerobase.tablereservation.visitor.dto.ReservationMessage;
import com.zerobase.tablereservation.visitor.dto.ReserveDto;
import com.zerobase.tablereservation.visitor.dto.ReserveRecord;
import com.zerobase.tablereservation.visitor.dto.ReviewMessage;
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

    private String DELETE_MESSAGE_SUCCESS = "성공적으로 예약을 삭제했습니다";
    private String DELETE_REVIEW_MESSAGE_SUCCESS = "리뷰가 삭제되었습니다";

    @Override
    public ReservationMessage.Response makeReservation(ReservationMessage.Request request, MemberEntity member) {

        StoreEntity store = storeRepository.findByStoreId(request.getStoreId())
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));

        // 해당 상점이 여는 시간에 예약을 했는지 확인한다
        LocalDateTime reserveDate = request.getReserveDate();

        if (!validTime(reserveDate, store.getOpenTime(), store.getLastReserveTime(), store.getBreakStart(), store.getBreakEnd())) {
            throw new ReserveException(ErrorCode.INVALID_RESERVE_TIME);
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

        return ReservationMessage.Response.fromDto(reserveDto);
    }

    @Override
    public List<ReserveRecord.Response> getMyReservations(MemberEntity member) {

        // 예약상태 확인 (Before을 확인해서, status를 바꿀 것이 있는지 확인 및 변경)
        List<ReserveEntity> reserve = reserveRepository.findAllByMemberEntityAndServiceUse(member, ServiceUseStatus.BEFORE);

        checkReservationStatus(reserve);

        // 최종적으로 해당 유저의 모든 정보를 리스트에 넣어서 응답하기
        return getReservationList(member);
    }

    @Override
    public ReservationMessage.Response updateReservation(ReservationMessage.RequestUpdate request, MemberEntity member) {
        // 유효한 예약 내역인지 확인한다
        ReserveEntity reserve = reserveRepository.findById(request.getReservationNum())
                .orElseThrow(() -> new StoreException(ErrorCode.RESERVATION_NOT_FOUND));

        // 예약 내역이 로그인 한 유저가 작성한 것인지 확인
        if (!validUserForReserve(reserve, member))
            throw new ReserveException(ErrorCode.UNMATCHING_USER_AND_RESERVATION);

        // 매장이 유효한지 확인
        StoreEntity store = storeRepository.findByStoreId(request.getStoreId())
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));

        // SERVICE_USE가 BEFORE인지 확인 (BEFORE에만 수정 가능)
        if (reserve.getServiceUse() != ServiceUseStatus.BEFORE)
            throw new ReserveException(ErrorCode.CANCELED_OR_ALREADY_USED_RESERVATION);

        // 바꾼 예약 시간이 유효한지 확인
        if (!validTime(request.getReserveDate(), store.getOpenTime(), store.getLastReserveTime(), store.getBreakStart(), store.getBreakEnd()))
            throw new ReserveException(ErrorCode.INVALID_RESERVE_TIME);

        reserve.setStoreEntity(store);
        reserve.setReserveDate(request.getReserveDate());
        reserve.setPeopleNum(request.getPeopleNum());
        reserveRepository.save(reserve);

        return ReservationMessage.Response.fromDto(ReserveDto.fromEntity(reserve));
    }

    @Override
    public String cancelReservation(Long reserveNum, MemberEntity member) {

        // 예약 entity 가지고 오기
        ReserveEntity reserve = reserveRepository.findById(reserveNum)
                .orElseThrow(() -> new ReserveException(ErrorCode.RESERVATION_NOT_FOUND));

        // 예약 내역을 삭제하려는 로그인한 유저가, 작성한 예약 내역인가?
        if (!validUserForReserve(reserve, member))
            throw new ReserveException(ErrorCode.UNMATCHING_USER_AND_RESERVATION);

        reserveRepository.delete(reserve);

        return DELETE_MESSAGE_SUCCESS;
    }

    @Override
    public ReviewMessage.Response writeReview(ReviewMessage.Request request, MemberEntity member) {

        // request에 있는 예약 번호를 통해서 예약 엔티티를 찾는다
        ReserveEntity reserve = reserveRepository.findById(request.getReservationId())
                .orElseThrow(() -> new ReserveException(ErrorCode.RESERVATION_NOT_FOUND));

        // 리뷰를 작성할 수 있는지 확인한다
        validReviewCheck(reserve, member);

        // 리뷰가 작성이 되어 있으면, 다시 작성을 못 하게 한다
        if (reserve.getReview() != null)
            throw new ReserveException(ErrorCode.ALREADY_WRITTEN_REVIEW);

        reserve.setReview(request.getReview());
        reserveRepository.save(reserve);

        return ReviewMessage.Response.fromDto(ReserveDto.fromEntity(reserve));
    }

    @Override
    public List<ReviewMessage.Response> getAllUserReview(MemberEntity member) {

        List<ReviewMessage.Response> result = new ArrayList<>();

        List<ReserveEntity> reserveList = reserveRepository.findAllByMemberEntityAndReviewIsNotNull(member);

        for (ReserveEntity reserve : reserveList) {
            result.add(ReviewMessage.Response.fromDto(ReserveDto.fromEntity(reserve)));
        }

        return result;
    }

    @Override
    public ReviewMessage.Response updateReview(ReviewMessage.Request request, MemberEntity member) {

        // request에 있는 예약 번호를 통해서 예약 엔티티를 찾는다
        ReserveEntity reserve = reserveRepository.findById(request.getReservationId())
                .orElseThrow(() -> new ReserveException(ErrorCode.RESERVATION_NOT_FOUND));

        // 리뷰를 작성할 수 있는지 확인한다
        validReviewCheck(reserve, member);

        // 리뷰가 작성이 되어 있어야, 리뷰를 수정할 수 있도록 한다
        if (reserve.getReview() == null)
            throw new ReserveException(ErrorCode.NULL_REVIEW);

        reserve.setReview(request.getReview());
        reserveRepository.save(reserve);

        return ReviewMessage.Response.fromDto(ReserveDto.fromEntity(reserve));
    }

    // 메서드 이름만 delete이지 사실상 review를 null로 바꾸는 것
    @Override
    public String deleteReview(Long reserveNum, MemberEntity member) {

        // 예약 entity 가지고 오기
        ReserveEntity reserve = reserveRepository.findById(reserveNum)
                .orElseThrow(() -> new ReserveException(ErrorCode.RESERVATION_NOT_FOUND));

        // 삭제할 리뷰에 대한 예약 내역의 사람이, 로그인한 사람과 일치하는가?
        if (!validUserForReserve(reserve, member))
            throw new ReserveException(ErrorCode.UNMATCHING_USER_AND_RESERVATION);

        reserve.setReview(null);
        reserveRepository.save(reserve);

        return DELETE_REVIEW_MESSAGE_SUCCESS;
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
            int breakEMin = Integer.parseInt(breakE[0]) * 60 + Integer.parseInt(breakE[1]);

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

    private boolean validUserForReserve(ReserveEntity reserve, MemberEntity member) {

        if (!reserve.getMemberEntity().getUsername().equals(member.getUsername())) return false;

        return true;
    }

    private void validReviewCheck(ReserveEntity reserve, MemberEntity member) {

        // 예약을 한 유저와, 로그한 유저가 일치한지 확인한다
        if (!validUserForReserve(reserve, member))
            throw new ReserveException(ErrorCode.UNMATCHING_USER_AND_RESERVATION);

        // 예약의 SERVICE_USE가 BEFORE 또는 USED일 때에만 review를 쓸 수 있다
        if (reserve.getServiceUse() != ServiceUseStatus.USED)
            throw new ReserveException(ErrorCode.INVALID_CONDITION_TO_WRITE_REVIEW);
    }

}
