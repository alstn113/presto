import { useEffect, useState } from 'react';
import type {
  ChatMessageReceivedEvent,
  TypingStatusChangedEvent,
} from '../../../hooks/socket/types';
import useChatMessageSocket from '../../../hooks/socket/useChatMessageSocket';
import useChatTypingStatusSocket from '../../../hooks/socket/useChatTypingStatusSocket';
import TypingIndicator from './TypingIndicator';
import MessageList from './MessageList';
import MessageInput from './MessageInput';
import useUpdateEffect from '../../../hooks/useUpdateEffect';
import useChatMessagesCursorInfiniteQuery from '../../../hooks/chat/useChatMessagesCursorInfiniteQuery';
import useGetChatRoomOverview from '../../../hooks/chat/useGetChatRoomOverview';

interface ChatRoomViewProps {
  selectedChatRoom: { id: string; name: string };
}

const ChatRoomView = ({ selectedChatRoom }: ChatRoomViewProps) => {
  const [messages, setMessages] = useState<ChatMessageReceivedEvent[]>([]);
  const [input, setInput] = useState('');
  const [typingUserIds, setTypingUserIds] = useState<Set<string>>(new Set());

  // suspense query 먼저 실행해야함.
  const { data: initialData } = useGetChatRoomOverview({
    chatRoomId: selectedChatRoom.id,
  });

  // infinite query 나중에 실행해야함. initialData에 의존
  // useGetChatRoomOverview가 완료되어야 initialData가 유효하므로,
  // 이 훅은 useGetChatRoomOverview가 완료된 후에 제대로 동작할 것입니다.
  const { data } = useChatMessagesCursorInfiniteQuery({
    chatRoomId: selectedChatRoom.id,
    initialData: {
      messages: initialData.messages,
      prevCursor: initialData.prevCursor,
      nextCursor: initialData.nextCursor,
    },
  });

  // ---- 기존 첫 번째 useEffect 수정 ----
  useEffect(() => {
    console.log(
      `[ChatRoomView] selectedChatRoom changed to: ${selectedChatRoom.id}`
    );
    // 채팅방이 변경될 때 로컬 상태 초기화
    setMessages([]);
    setTypingUserIds(new Set());
    setInput('');

    // useGetChatRoomOverview의 initialData가 있을 경우 메시지 상태를 즉시 채움
    // initialData는 캐시에서 올 수도 있고, 새로 가져온 데이터일 수도 있습니다.
    console.log(`[ChatRoomView] initialData available: ${!!initialData}`);
    if (initialData?.messages?.length) {
      console.log(
        `[ChatRoomView] Populating messages from initialData. Count: ${initialData.messages.length}`
      );
      setMessages(initialData.messages);
    } else {
      console.log(
        `[ChatRoomView] initialData has no messages or not yet available.`
      );
    }
  }, [selectedChatRoom, initialData]); // selectedChatRoom과 initialData 모두 의존성에 추가

  // ---- 기존 두 번째 useEffect (infinite query data 처리) 그대로 유지 ----
  useEffect(() => {
    console.log(
      `[ChatRoomView] Infinite query data updated. Data available: ${!!data}`
    );
    if (!data) {
      console.log('[ChatRoomView] Infinite query data is null or undefined.');
      return;
    }
    console.log(
      `[ChatRoomView] Infinite query pages length: ${data.pages?.length}`
    );
    if (data.pages?.length) {
      console.log(
        '[ChatRoomView] Processing messages from infinite query data.'
      );
      const allMessages = data.pages.flatMap((page) => page.messages);
      // 상태를 완전히 교체하는 대신, 기존 메시지와 합치거나 필요한 로직을 고려할 수 있습니다.
      // 여기서는 단순히 모든 페이지의 메시지를 다시 설정합니다.
      setMessages(allMessages);
      console.log(
        `[ChatRoomView] Messages state updated from infinite data. Total count: ${allMessages.length}`
      );
    } else {
      console.log(
        '[ChatRoomView] Infinite query data has no pages or pages is empty.'
      );
    }
  }, [data]); // data에 의존

  // ... (나머지 코드 동일) ...

  const handleSendChatMessage = (message: ChatMessageReceivedEvent) => {
    setMessages((prev) => [...prev, message]);
  };

  const handleTypingStatus = ({
    isTyping,
    senderId,
  }: TypingStatusChangedEvent) => {
    setTypingUserIds((prev) => {
      const updated = new Set(prev);

      if (isTyping) updated.add(senderId);
      else updated.delete(senderId);

      return updated;
    });
  };

  const { sendChatMessage } = useChatMessageSocket(
    selectedChatRoom.id,
    handleSendChatMessage
  );
  const { sendTypingStatus } = useChatTypingStatusSocket(
    selectedChatRoom.id,
    handleTypingStatus
  );

  const sendMessage = () => {
    if (!input.trim()) return;
    sendChatMessage({ content: input, chatRoomId: selectedChatRoom.id });
    setInput('');
  };

  useUpdateEffect(() => {
    if (input) {
      sendTypingStatus({ chatRoomId: selectedChatRoom.id, isTyping: true });
      const timeout = setTimeout(() => {
        sendTypingStatus({ chatRoomId: selectedChatRoom.id, isTyping: false });
      }, 500);
      return () => clearTimeout(timeout);
    } else {
      sendTypingStatus({ chatRoomId: selectedChatRoom.id, isTyping: false });
    }
  }, [input]);

  return (
    <div className="w-full mx-auto p-4 bg-white shadow rounded-lg h-full flex flex-col">
      <h2 className="text-xl font-semibold mb-4">
        채팅방 - {selectedChatRoom.name}
      </h2>
      <MessageList messages={messages} />
      <TypingIndicator typingUserIds={typingUserIds} />
      <MessageInput input={input} setInput={setInput} onSend={sendMessage} />
    </div>
  );
};

export default ChatRoomView;
