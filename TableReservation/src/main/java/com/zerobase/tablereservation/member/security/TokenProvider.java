package com.zerobase.tablereservation.member.security;

import com.zerobase.tablereservation.member.service.impl.MemberServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final MemberServiceImpl memberServiceImpl;
    private static final String KEY_ROLES = "roles";
    private static final long VALIDATE_TIME = 1 * 60 * 60 * 1000L; // 1시간

    @Value("${spring.jwt.secret}")
    private String secretKey;

    // 토큰 생성 매서드
    public String generateToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles); // key value로 저장

        var now = new Date();
        var expiredDate = new Date(now.getTime() + VALIDATE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성시간
                .setExpiration(expiredDate) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘, 비밀키
                .compact();
    }

    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails = memberServiceImpl.loadUserByUsername(getUsername(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return this.parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        // 토큰이 빈 문자열이면 false
        if (!StringUtils.hasText(token)) return false;

        var claims = parseClaims(token);
        return !claims.getExpiration().before(new Date()); //토큰 만료 시간이 현재보다 이전인지 아닌지 만료 여부 확인
    }

    // 토큰이 유효한지 확인하는 메서드
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            // 파싱하는 과정에서 토큰 만료 시간이 지날 수 있다, 만료된 토큰을 확인할 때에
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
