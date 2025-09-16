package com.mcphub.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import com.mcphub.global.common.base.BaseEntity;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;
}