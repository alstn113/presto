package com.presto.server.application.auth;

import com.presto.server.application.auth.request.LoginRequest;
import com.presto.server.application.auth.request.RegisterRequest;
import com.presto.server.application.auth.response.MemberDetailsResponse;
import com.presto.server.application.auth.response.TokenResponse;
import com.presto.server.domain.member.Member;
import com.presto.server.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public MemberDetailsResponse getMemberDetails(Long id) {
        Member member = getById(id);

        return new MemberDetailsResponse(member.getId(), member.getUsername(), member.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        Member member = getByUsername(request.username());

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = tokenProvider.createToken(member.getId());
        return new TokenResponse(token);
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (memberRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = new Member(request.username(), encodedPassword);
        memberRepository.save(member);
    }

    private Member getByUsername(String username) {
        return memberRepository
                .findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다. 사용자 이름: %s".formatted(username)));
    }

    private Member getById(Long id) {
        return memberRepository
                .findById(id)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다. 사용자 식별자: %d".formatted(id)));
    }
}
