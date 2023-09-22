package com.zerobase.tablereservation.member.service.impl;

import com.zerobase.tablereservation.member.domain.Member;
import com.zerobase.tablereservation.member.dto.MemberDto;
import com.zerobase.tablereservation.member.dto.MemberRegister;
import com.zerobase.tablereservation.member.repository.MemberRepository;
import com.zerobase.tablereservation.member.service.MemberService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Optional;

public class MemberServiceImpl implements MemberService {

    MemberRepository memberRepository;

    private String ownerOrCustomer(String result) {
        if (result.equals("0")) return Member.OWNER;
        if (result.equals("1")) return Member.CUSTOMER;

        throw new RuntimeException("0 또는 1을 입력해주세요.\n0 = 매장 운영자\n1 = 매장 이용자");
    }

    @Override
    public MemberRegister.Response register(MemberRegister.Request request) throws RuntimeException {

        Optional<Member> memberCheck = memberRepository.findByUsername(request.getUsername());

        if (memberCheck.isPresent()) {
            throw new RuntimeException("유저가 이미 존재합니다");
        }

        // 매장 운영자인지 또는 매장 이용자인지 확인
        String ownerCustomer = ownerOrCustomer(request.getOwnerOrCustomer());

        String passwordEncode = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        Member member = Member.builder()
                .username(request.getUsername())
                .password(passwordEncode)
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .name(request.getName())
                .ownerOrCustomer(ownerCustomer)
                .registeredAt(LocalDateTime.now())
                .build();

        memberRepository.save(member);

        MemberDto memberDto = MemberDto.fromEntity(member);

        return MemberRegister.Response.from(memberDto);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
