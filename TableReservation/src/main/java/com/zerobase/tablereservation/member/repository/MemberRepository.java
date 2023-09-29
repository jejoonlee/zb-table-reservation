package com.zerobase.tablereservation.member.repository;

import com.zerobase.tablereservation.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

        Optional<MemberEntity> findByUsername(String username);

        boolean existsByUsername(String username);
}
