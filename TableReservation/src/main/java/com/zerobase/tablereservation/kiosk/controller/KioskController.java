package com.zerobase.tablereservation.kiosk.controller;

import com.zerobase.tablereservation.kiosk.dto.KioskRegister;
import com.zerobase.tablereservation.kiosk.service.impl.KioskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kiosk")
@RequiredArgsConstructor
public class KioskController {

    private final KioskServiceImpl kioskService;

    // 예약번호와 예약한 사람의 username을 받는다
    @PostMapping("/check")
    public String check(
            @RequestBody KioskRegister.Request request
    ) {
        return kioskService.checkReservation(request.getReservationNum(), request.getUsername());
    }

}
