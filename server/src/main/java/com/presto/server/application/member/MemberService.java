package com.presto.server.application.member;

import com.presto.server.application.member.response.MemberInfoResponse;
import com.presto.server.domain.member.Member;
import com.presto.server.domain.member.MemberRepository;
import com.presto.server.support.error.CoreException;
import com.presto.server.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CoreException(ErrorType.MEMBER_NOT_FOUND));

        return new MemberInfoResponse(
                member.getId(),
                member.getUsername(),
                member.getCreatedAt()
        );
    }
}
