package com.zerobase.tablereservation.member.service.impl;

import com.zerobase.tablereservation.member.domain.Member;
import com.zerobase.tablereservation.member.dto.Login;
import com.zerobase.tablereservation.member.dto.MemberDto;
import com.zerobase.tablereservation.member.dto.MemberRegister;
import com.zerobase.tablereservation.member.repository.MemberRepository;
import com.zerobase.tablereservation.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private String ownerOrCustomer(String result) {
        if (result.equals("0")) return "STORE_OWNER";
        if (result.equals("1")) return "STORE_USER";

        throw new RuntimeException("0 또는 1을 입력해주세요.\n0 = 매장 운영자\n1 = 매장 이용자");
    }

    @Override
    public MemberRegister.Response register(MemberRegister.Request request) throws RuntimeException {

        boolean memberExist = memberRepository.existsByUsername(request.getUsername());

        if (memberExist) {
            throw new RuntimeException("유저가 이미 존재합니다");
        }

        // 매장 운영자인지 또는 매장 이용자인지 확인
        String ownerCustomer = ownerOrCustomer(request.getOwnerOrCustomer());

        List<String> role = new ArrayList<>();
        role.add(ownerCustomer);

        Member member = Member.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .name(request.getName())
                .ownerOrCustomer(role)
                .registeredAt(LocalDateTime.now())
                .build();

        memberRepository.save(member);

        MemberDto memberDto = MemberDto.fromEntity(member);

        return MemberRegister.Response.from(memberDto);
    }

    @Override
    public Login.Response login(Login.Request request) {
        Member user = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        // user에 들어간 비밀번호는 encoding이 되어 있다

        // encoding된 비밀번호를 먼저 확인
        // 다르면 예외발생
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        return Login.Response.from(MemberDto.fromEntity(user));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다"));
    }
}
