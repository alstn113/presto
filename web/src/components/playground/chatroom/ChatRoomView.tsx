import { useEffect, useLayoutEffect, useRef, useState } from 'react';
import type {
  ChatMessageReceivedEvent,
  TypingStatusChangedEvent,
} from '../../../hooks/socket/types';
import useChatMessageSocket from '../../../hooks/socket/useChatMessageSocket';
import useChatTypingStatusSocket from '../../../hooks/socket/useChatTypingStatusSocket';
import TypingIndicator from './TypingIndicator';
import MessageInput from './MessageInput';
import useUpdateEffect from '../../../hooks/useUpdateEffect';
import useChatMessagesCursorInfiniteQuery from '../../../hooks/chat/useChatMessagesCursorInfiniteQuery';
import useGetChatRoomOverview from '../../../hooks/chat/useGetChatRoomOverview';
import { formatRelativeDate } from '../../../libs/utils/dateUtils';
import useIntersectionObserver from '../../../hooks/useIntersectionObserver';
import useChatRoomParticipantInfo from '../../../hooks/chat/useChatRoomParticipantInfo';

interface ChatRoomViewProps {
  selectedChatRoom: { id: string; name: string };
}

const ChatRoomView = ({ selectedChatRoom }: ChatRoomViewProps) => {
  const [messages, setMessages] = useState<ChatMessageReceivedEvent[]>([]);
  const [input, setInput] = useState('');
  const [typingUserIds, setTypingUserIds] = useState<Set<string>>(new Set());

  const { data: initialData } = useGetChatRoomOverview({
    chatRoomId: selectedChatRoom.id,
  });

  const {
    data,
    isFetchingPreviousPage,
    isFetchingNextPage,
    fetchPreviousPage,
    fetchNextPage,
    hasPreviousPage,
    hasNextPage,
  } = useChatMessagesCursorInfiniteQuery({
    chatRoomId: selectedChatRoom.id,
    initialData: {
      messages: initialData.messages,
      prevCursor: initialData.prevCursor,
      nextCursor: initialData.nextCursor,
    },
  });

  useEffect(() => {
    setMessages([]);
    setTypingUserIds(new Set());
    setInput('');

    if (initialData?.messages?.length) {
      setMessages(initialData.messages);
    }
  }, [selectedChatRoom, initialData]);

  useEffect(() => {
    if (!data) return;
    if (data.pages?.length) {
      const allMessages = data.pages.flatMap((page) => page.messages);
      setMessages(allMessages);
    }
  }, [data]);

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

  const { getParticipantById } = useChatRoomParticipantInfo();
  const containerRef = useRef<HTMLDivElement>(null);
  const scrollHeightBeforeUpdate = useRef(0);

  const prepareForAppendTop = () => {
    if (containerRef.current) {
      scrollHeightBeforeUpdate.current = containerRef.current.scrollHeight;
    }
  };

  useLayoutEffect(() => {
    if (containerRef.current) {
      const container = containerRef.current;
      const newHeight = container.scrollHeight;
      const diff = newHeight - scrollHeightBeforeUpdate.current;
      container.scrollTop += diff;
    }
  }, [messages]);

  const topRef = useIntersectionObserver({
    onIntersect: () => {
      if (hasPreviousPage && !isFetchingPreviousPage) {
        console.log('Fetching previous page...');
        prepareForAppendTop();
        fetchPreviousPage();
      }
    },
    hasNextPage: hasPreviousPage,
    threshold: 0.5,
    delayMs: 300,
  });

  const bottomRef = useIntersectionObserver({
    onIntersect: () => {
      if (hasNextPage && !isFetchingNextPage) {
        console.log('Fetching next page...');
        fetchNextPage();
      }
    },
    hasNextPage: hasNextPage,
    threshold: 0.5,
    delayMs: 300,
  });

  return (
    <div className="w-full mx-auto p-4 bg-white shadow rounded-lg h-full flex flex-col">
      <h2 className="text-xl font-semibold mb-4">
        채팅방 - {selectedChatRoom.name}
      </h2>
      <div
        ref={containerRef}
        className="min-h-[400px] max-h-[400px] overflow-y-auto border border-gray-300 rounded p-4 mb-4 space-y-2 bg-gray-50"
      >
        <div ref={topRef} />

        {[...messages].map((msg) =>
          renderMessage({
            msg,
            username: getParticipantById(msg.senderId)?.username || 'Unknown',
          })
        )}

        <div ref={bottomRef} />
      </div>
      <TypingIndicator typingUserIds={typingUserIds} />
      <MessageInput input={input} setInput={setInput} onSend={sendMessage} />
    </div>
  );
};

const renderMessage = ({
  msg,
  username,
}: {
  msg: ChatMessageReceivedEvent;
  username: string;
}) => {
  const time = formatRelativeDate(msg.sentAt);

  switch (msg.messageType) {
    case 'TEXT':
      return (
        <li
          key={msg.id}
          className="p-2 rounded border border-gray-200 bg-white flex justify-between items-start"
        >
          <div>
            <span className="font-bold text-blue-600">{username}:</span>
            <span className="text-gray-800">{msg.content}</span>
          </div>
          <span className="text-xs text-gray-400 ml-2 whitespace-nowrap">
            {time}
          </span>
        </li>
      );

    case 'SYSTEM':
      return (
        <li
          key={msg.id}
          className="text-center text-sm text-gray-500 italic py-1"
        >
          📢 {msg.content}
          <span className="text-xs text-gray-400 ml-1">({time})</span>
        </li>
      );

    default:
      return (
        <li key={msg.id} className="text-red-500">
          알 수 없는 메시지 유형입니다.
        </li>
      );
  }
};

export default ChatRoomView;
