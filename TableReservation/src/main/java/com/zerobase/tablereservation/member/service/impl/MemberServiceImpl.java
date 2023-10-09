package com.zerobase.tablereservation.member.service.impl;

import com.zerobase.tablereservation.exception.ErrorCode;
import com.zerobase.tablereservation.exception.MemberException;
import com.zerobase.tablereservation.member.domain.MemberEntity;
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

        throw new MemberException(ErrorCode.UNSUCCESSFUL_ROLE_INPUT);
    }

    @Override
    public MemberRegister.Response register(MemberRegister.Request request) throws RuntimeException {

        boolean memberExist = memberRepository.existsByUsername(request.getUsername());


        if (memberExist) {
            throw new MemberException(ErrorCode.EXISTING_USER);
        }

        List<String> role = new ArrayList<>();
        role.add(ownerOrCustomer(request.getRole()));

        // 비밀번호 인코딩하기
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        MemberEntity memberEntity = request.toEntity();
        memberEntity.setRole(role);

        memberRepository.save(memberEntity);

        MemberDto memberDto = MemberDto.fromEntity(memberEntity);

        return MemberRegister.Response.from(memberDto);
    }

    @Override
    public Login.Response login(Login.Request request) {
        MemberEntity user = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new MemberException(ErrorCode.USER_NOT_FOUND));

        // user에 들어간 비밀번호는 encoding이 되어 있다
        // encoding된 비밀번호를 먼저 확인
        // 다르면 예외발생
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new MemberException(ErrorCode.UNMATCHING_PASSWORD);
        }

        return Login.Response.from(MemberDto.fromEntity(user));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorCode.USER_NOT_FOUND));
    }
}
