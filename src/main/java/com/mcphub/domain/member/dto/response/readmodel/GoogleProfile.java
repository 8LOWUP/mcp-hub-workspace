package com.mcphub.domain.member.dto.response.readmodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleProfile {
    private String id;
    private String email;
    private String name; // Google은 name을 주니까 → nickname으로 쓸 예정
}
