package com.mcphub.domain.member.mapper;


import org.springframework.stereotype.Component;


@Component
public class MemberMapper {


    /**
     *
     * @param centralPosition    총괄/부총괄/... etc(중앙 직책)
     * @param universityPosition 회장/부회장/~파트장/... etc(교내 운영진 직책)
     * @return Role (권한)
     */
    private String determineRole(
            String centralPosition,
            String universityPosition
    ) {
        if (centralPosition == null && universityPosition == null) {
            return "CHALLENGER";
        }
        if (centralPosition != null && (centralPosition.equals("총괄") || centralPosition.equals("부총괄"))) {
            return "ADMIN";
        }
        if (centralPosition != null) {
            return "CENTRAL_ADMIN";
        }
        if (universityPosition != null && universityPosition.equals("회장") || universityPosition.equals("부회장")) {
            return "SCHOOL_ADMIN";
        }
        if (universityPosition != null) {
            return "UNIVERSITY_STAFF";
        }
        return "CHALLENGER";
    }
}

