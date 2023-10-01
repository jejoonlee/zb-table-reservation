package com.zerobase.tablereservation.store.service.impl;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.member.repository.MemberRepository;
import com.zerobase.tablereservation.store.domain.StoreEntity;
import com.zerobase.tablereservation.store.dto.StoreDto;
import com.zerobase.tablereservation.store.dto.StoreRegister;
import com.zerobase.tablereservation.store.dto.StoreSearch;
import com.zerobase.tablereservation.store.repository.StoreRepository;
import com.zerobase.tablereservation.store.service.StoreService;
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
    public StoreSearch.StoreDetailResponse getStore(Long storeId) {

        StoreEntity store = storeRepository.findByStoreId(storeId)
                .orElseThrow(() -> new RuntimeException("해당 매장은 등록이 안 되어 있습니다"));

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
}
