package com.presto.server.application.chat.message;

import com.presto.server.application.chat.message.request.TypingStatusRequest;
import com.presto.server.application.chat.message.response.TypingStatusChangedEvent;
import com.presto.server.application.chat.message.response.TypingStatusChangedEvent.Sender;
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

        String destination = "/topic/chat/%s/typing".formatted(request.chatRoomId());
        TypingStatusChangedEvent event = new TypingStatusChangedEvent(
                request.chatRoomId(),
                new Sender(member.getId(), member.getUsername()),
                request.isTyping()
        );
        messagingTemplate.convertAndSend(destination, event);
    }

    private Member getMemberById(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CoreException(ErrorType.MEMBER_NOT_FOUND));
    }
}
