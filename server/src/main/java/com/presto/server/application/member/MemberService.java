package com.presto.server.application.member;

import com.presto.server.application.auth.response.MemberDetailsResponse;
import com.presto.server.domain.member.Member;
import com.presto.server.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberDetailsResponse getMemberInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        return new MemberDetailsResponse(member.getId(), member.getUsername(), member.getCreatedAt());

    }
}
