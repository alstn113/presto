package com.presto.server.member.ui;

import com.presto.server.auth.application.response.MemberDetailsResponse;
import com.presto.server.member.application.MemberService;
import com.presto.server.security.Accessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberV1Controller {

    private final MemberService memberService;

    @GetMapping("/api/v1/members/me")
    public ResponseEntity<MemberDetailsResponse> getMyInfo(@AuthenticationPrincipal Accessor accessor) {
        MemberDetailsResponse response = memberService.getMemberInfo(accessor.id());

        return ResponseEntity.ok(response);
    }
}
