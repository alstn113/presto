package com.presto.server.auth.application;

import com.presto.server.auth.application.response.MemberInfoResponse;
import com.presto.server.user.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberInfoResponse getMember(Long id) {
        return memberRepository
                .findById(id)
                .map(it -> new MemberInfoResponse(it.getId(), it.getUsername(), it.createdAt()))
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다. 사용자 식별자: %d".formatted(id)));
    }
}
