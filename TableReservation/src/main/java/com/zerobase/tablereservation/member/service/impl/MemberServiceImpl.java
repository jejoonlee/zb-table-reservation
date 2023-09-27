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

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private String ownerOrCustomer(String result) {
        if (result.equals("0")) return "ROLE_STORE_OWNER";
        if (result.equals("1")) return "ROLE_STORE_USER";

        throw new RuntimeException("0 또는 1을 입력해주세요.\n0 = 매장 운영자\n1 = 매장 이용자");
    }

    @Override
    public MemberRegister.Response register(MemberRegister.Request request) throws RuntimeException {

        boolean memberExist = memberRepository.existsByUsername(request.getUsername());


        if (memberExist) {
            throw new RuntimeException("유저가 이미 존재합니다");
        }

        List<String> role = new ArrayList<>();
        role.add(ownerOrCustomer(request.getRole()));

        // 비밀번호 인코딩하기
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        Member member = request.toEntity();
        member.setRole(role);

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
