package com.zerobase.tablereservation.store.service.impl;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.member.repository.MemberRepository;
import com.zerobase.tablereservation.store.domain.StoreEntity;
import com.zerobase.tablereservation.store.dto.StoreDto;
import com.zerobase.tablereservation.store.dto.StoreRegister;
import com.zerobase.tablereservation.store.repository.StoreRepository;
import com.zerobase.tablereservation.store.service.StoreService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Setter
@Getter
@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    @Override
    public StoreRegister.Response registerStore(StoreRegister.Request request, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        StoreEntity storeEntity = request.toEntity();

        MemberEntity member = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("로그인이 안 되어 있습니다"));

        storeEntity.setUsername(member);
        storeEntity.setRegisteredAt(LocalDateTime.now());

        storeRepository.save(storeEntity);

        StoreDto storeDto = StoreDto.fromEntity(storeEntity);

        return StoreRegister.Response.from(storeDto);
    }
}
