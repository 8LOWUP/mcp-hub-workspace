package com.mcphub.domain.member.repository.Jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mcphub.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);
}