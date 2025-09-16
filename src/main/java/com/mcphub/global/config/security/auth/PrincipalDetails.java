package com.mcphub.global.config.security.auth;

import com.mcphub.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@AllArgsConstructor
public class PrincipalDetails implements UserDetails {
    private Long memberId;

    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한을 사용하지 않으므로 빈 리스트 반환
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        // 소셜 로그인만 지원하므로 비밀번호는 필요 없음
        return "";
    }

    @Override
    public String getUsername() {
        // member의 id를 username으로 사용
        return memberId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

