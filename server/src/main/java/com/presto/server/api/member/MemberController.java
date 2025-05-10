package com.presto.server.api.member;

import com.presto.server.application.auth.response.MemberDetailsResponse;
import com.presto.server.application.member.MemberService;
import com.presto.server.infra.security.Accessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members/me")
    public ResponseEntity<MemberDetailsResponse> getMyInfo(@AuthenticationPrincipal Accessor accessor) {
        MemberDetailsResponse response = memberService.getMemberInfo(accessor.id());

        return ResponseEntity.ok(response);
    }
}
