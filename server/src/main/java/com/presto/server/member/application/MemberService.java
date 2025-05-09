package com.presto.server.member.application;

import com.presto.server.auth.application.response.MemberInfoResponse;
import com.presto.server.member.domain.Member;
import com.presto.server.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        return new MemberInfoResponse(member.getId(), member.getUsername(), member.getCreatedAt());

    }
}
