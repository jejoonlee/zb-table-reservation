package com.zerobase.tablereservation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다."),
    INVALID_REQUEST("잘 못된 요청입니다"),
    NO_INPUT("빈칸 값이 있습니다"),
    WRONG_DATE_INPUT("날짜 입력을 잘 못 했습니다"),
    WRONG_INPUT_REQUEST("입력값을 제대로 입력하지 않았습니다. 다시 확인해주세요"),
    JWT_SHOULD_NOT_BE_TRUSTED("JWT 토큰의 유효성이 확인이 안 됩니다. 다시 입력해주세요"),

//    유저 관련 에러 코드
    USER_PERMISSION_NOT_GRANTED("해당 요청에 대한 권한이 없습니다. JWT 토큰을 헤더에 추가하거나, 다른 로그인 정보로 로그인해주세요"),
    USER_NOT_LOGGED_IN("로그인이 제대로 안 되어 있습니다"),
    USER_NOT_FOUND("해당 유저에 대한 정보가 없습니다"),
    UNSUCCESSFUL_ROLE_INPUT("0 또는 1을 입력해주세요.\n0 = 매장 운영자\n1 = 매장 이용자"),
    EXISTING_USER("유저가 이미 존재합니다"),
    UNMATCHING_PASSWORD("비밀번호가 일치하지 않습니다"),


//    스토어 관련 에러 코드
    STORE_NOT_FOUND("상점을 찾을 수 없습니다"),
    UNMATCHING_USER_AND_STORE_OWNER("로그인한 유저와 매장 점주가 다릅니다"),
    KEYWORD_NOT_FOUND("검색한 키워드에 대한 상점이 없습니다"),

//    예약 관련 에러 코드
    RESERVATION_NOT_FOUND("예약한 내역이 존재하지 않습니다"),
    INVALID_RESERVE_TIME("예약을 할 수 없는 시간입니다"),
    UNMATCHING_USER_AND_RESERVATION("예약한 유저와 로그인한 유저가 같아야 합니다"),
    UNMATCHING_REQUESTED_USER_AND_RESERVATION("입력한 예약자의 이름과 예약 내역의 유저 이름이 일치하지 않습니다"),
    CANCELED_OR_ALREADY_USED_RESERVATION("예약이 취소 되었거나, 이미 사용한 내역이 있는 예약입니다"),
    ALREADY_WRITTEN_REVIEW("리뷰가 이미 작성이 되어 있는 상태입니다"),
    NULL_REVIEW("수정할 리뷰 내용이 없습니다. 리뷰를 작성해주세요"),
    INVALID_CONDITION_TO_WRITE_REVIEW("예약을 하고, 매장을 이용해야 리뷰를 작성하실 수 있습니다"),

//    키오스크 관련 에러 코드
    CANCEL_BY_TIME_MESSAGE("예약 10분전까지만 예약 확인이 가능합니다\n예약이 자동으로 취소 되었습니다"),
    CANCELED_RESERVE("취소가 된 예약이거나, 이미 사용한 예약입니다");

    private String description;
}
