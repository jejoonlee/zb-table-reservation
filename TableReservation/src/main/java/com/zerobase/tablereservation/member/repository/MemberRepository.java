package com.zerobase.tablereservation.member.repository;

import com.zerobase.tablereservation.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

        Optional<Member> findByUsername(String username);
}
