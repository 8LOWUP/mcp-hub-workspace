package com.mcphub.global.config.security.jwt;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import com.mcphub.global.config.security.auth.PrincipalDetails;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // 오직 인증 정보를 설정하는 역할만 수행

    private final JwtProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request); // 헤더에서 토큰을 받아옴

        if (token != null && jwtTokenProvider.validateToken(token)) { // 토큰이 유효하다면
            Authentication authentication = getAuthentication(token); // 인증 정보를 받아옴
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보를 설정
        }
        chain.doFilter(request, response); // 다음 필터로 넘김
    }

    private Authentication getAuthentication(String token) {
        Claims claims = jwtTokenProvider.getClaims(token);

        Long memberId = Long.valueOf(claims.get("memberId", String.class)); // memberId 가져옴

        // PrincipalDetails 사용
        PrincipalDetails principalDetails = new PrincipalDetails(memberId);

        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
    }
}

