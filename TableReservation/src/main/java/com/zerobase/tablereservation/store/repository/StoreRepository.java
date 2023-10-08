package com.zerobase.tablereservation.store.repository;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import com.zerobase.tablereservation.store.domain.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    Optional<StoreEntity> findByStoreId(Long storeId);

    List<StoreEntity> findAllByUsername(MemberEntity member);

    List<StoreEntity> findAllByStoreNameStartingWithIgnoreCase(String keyword);

}
