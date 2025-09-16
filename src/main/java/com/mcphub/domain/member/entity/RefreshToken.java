package com.mcphub.domain.member.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RefreshToken { //redis에 저장할 객체

    @Id
    @Indexed // 인덱스를 걸어주면 조회할 때 빠르게 찾을 수 있음
    private String memberId;
    @Indexed
    private String refreshToken;

    /*
    만료된 access Token을 가진 memberId 값으로  refresh Token을 찾아와서 유효성을 검사할 예정
     */
}

//memberId 값으로 refresh Token을 찾아와서 유효성을 검사한다.
