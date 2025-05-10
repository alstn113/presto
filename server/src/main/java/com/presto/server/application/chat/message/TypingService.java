package com.presto.server.application.chat.message;

import com.presto.server.application.chat.message.request.TypingStatusRequest;
import com.presto.server.application.chat.message.response.TypingStatusEvent;
import com.presto.server.application.chat.message.response.TypingStatusEvent.Sender;
import com.presto.server.domain.member.Member;
import com.presto.server.domain.member.MemberRepository;
import com.presto.server.support.error.CoreException;
import com.presto.server.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TypingService {

    private final SimpMessagingTemplate messagingTemplate;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public void sendMessage(TypingStatusRequest request) {
        Member member = getMemberById(request.accessor().id());

        String destination = "/topic/chat/%d/typing".formatted(request.chatRoomId());
        TypingStatusEvent event = new TypingStatusEvent(
                request.chatRoomId(),
                new Sender(member.getId(), member.getUsername()),
                request.isTyping()
        );
        messagingTemplate.convertAndSend(destination, event);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CoreException(ErrorType.MEMBER_NOT_FOUND));
    }
}
