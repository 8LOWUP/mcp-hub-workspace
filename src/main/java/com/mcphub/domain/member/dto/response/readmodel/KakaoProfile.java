package com.mcphub.domain.member.dto.response.readmodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoProfile {
    private Long id;
    private KakaoAccount kakao_account;

    @Getter
    @Setter
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter
        @Setter
        public static class Profile {
            private String nickname;
        }
    }
}
