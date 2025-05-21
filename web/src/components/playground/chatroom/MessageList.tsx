import type { ChatMessageReceivedEvent } from '../../../hooks/socket/types';
import { formatRelativeDate } from '../../../libs/utils/dateUtils';
import useChatRoomParticipantInfo from '../../../hooks/chat/useChatRoomParticipantInfo';
import useIntersectionObserver from '../../../hooks/useIntersectionObserver';
import { useRef } from 'react';

interface MessageListProps {
  messages: ChatMessageReceivedEvent[];
  isFetchingPreviousPage: boolean;
  isFetchingNextPage: boolean;
  fetchPreviousPage: () => void;
  fetchNextPage: () => void;
  hasPreviousPage: boolean;
  hasNextPage: boolean;
}

const MessageList = ({
  messages,
  isFetchingPreviousPage,
  isFetchingNextPage,
  fetchPreviousPage,
  fetchNextPage,
  hasPreviousPage,
  hasNextPage,
}: MessageListProps) => {
  const { getParticipantById } = useChatRoomParticipantInfo();
  const containerRef = useRef<HTMLDivElement>(null);
  const scrollHeightBeforeUpdate = useRef(0);

  const prepareForAppendTop = () => {
    if (containerRef.current) {
      scrollHeightBeforeUpdate.current = containerRef.current.scrollHeight;
    }
  };

  const topRef = useIntersectionObserver({
    onIntersect: async () => {
      if (hasPreviousPage && !isFetchingPreviousPage) {
        console.log('Fetching previous page...');
        prepareForAppendTop();
        fetchPreviousPage();
      }
    },
    threshold: 0.5,
  });

  const bottomRef = useIntersectionObserver({
    onIntersect: () => {
      if (hasNextPage && !isFetchingNextPage) {
        console.log('Fetching next page...');
        fetchNextPage();
      }
    },
    threshold: 0.5,
  });

  return (
    <div className="min-h-[400px] max-h-[400px] overflow-y-auto border border-gray-300 rounded p-4 mb-4 space-y-2 bg-gray-50">
      <div ref={topRef} className="text-center text-gray-500 py-2" />

      {[...messages].map((msg) =>
        renderMessage({
          msg,
          username: getParticipantById(msg.senderId)?.username || 'Unknown',
        })
      )}

      <div ref={bottomRef} className="text-center text-gray-500 py-2" />
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

export default MessageList;
