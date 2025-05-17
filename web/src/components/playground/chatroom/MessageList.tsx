import type { ChatMessageReceivedEvent } from '../../../hooks/socket/types';
import { formatRelativeDate } from '../../../libs/utils/dateUtils';
import useChatRoomParticipantInfo from '../../../hooks/chat/useChatRoomParticipantInfo';

interface Props {
  messages: ChatMessageReceivedEvent[];
}

const MessageList = ({ messages }: Props) => {
  const { getParticipantById } = useChatRoomParticipantInfo();
  const renderMessage = (msg: ChatMessageReceivedEvent) => {
    const time = formatRelativeDate(msg.sentAt);
    const username = getParticipantById(msg.senderId)?.username;

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
            📢 {msg.content}{' '}
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

  return (
    <ul className="min-h-[400px] max-h-[300px] overflow-y-auto border border-gray-300 rounded p-4 mb-4 space-y-2 bg-gray-50">
      {messages.map((msg) => renderMessage(msg))}
    </ul>
  );
};

export default MessageList;
