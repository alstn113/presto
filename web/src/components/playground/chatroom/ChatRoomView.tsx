import { useEffect, useState } from 'react';
import type {
  ChatMessageReceivedEvent,
  ChatMessageSendRequest,
  TypingStatusChangeRequest,
} from '../../../hooks/socket/types';
import useChatMessageSocket from '../../../hooks/socket/useChatMessageSocket';
import useChatTypingStatusSocket from '../../../hooks/socket/useChatTypingStatusSocket';
import TypingIndicator from './TypingIndicator';
import { socketClient } from '../../../libs/socket/socketClient';
import { SOCKET_PATHS } from '../../../constants';
import MessageList from './MessageList';
import MessageInput from './MessageInput';

interface ChatRoomViewProps {
  selectedChatRoom: { id: string; name: string };
}

const ChatRoomView = ({ selectedChatRoom }: ChatRoomViewProps) => {
  const [messages, setMessages] = useState<ChatMessageReceivedEvent[]>([]);
  const [input, setInput] = useState('');
  const [typingUsers, setTypingUsers] = useState<Record<string, string>>({});

  useEffect(() => {
    setMessages([]);
    setTypingUsers({});
    setInput('');
  }, [selectedChatRoom]);

  useChatMessageSocket(selectedChatRoom.id, (message) =>
    setMessages((prev) => [...prev, message])
  );
  useChatTypingStatusSocket(selectedChatRoom.id, ({ isTyping, sender }) => {
    setTypingUsers((prev) => {
      const { senderId, username } = sender;

      if (isTyping) {
        return {
          ...prev,
          [senderId]: username,
        };
      }

      const updated = { ...prev };
      delete updated[senderId];
      return updated;
    });
  });

  const sendMessage = () => {
    if (!input.trim()) return;
    socketClient.publish<ChatMessageSendRequest>(SOCKET_PATHS.CHAT.SEND, {
      content: input,
      chatRoomId: selectedChatRoom.id,
    });
    setInput('');
  };

  const sendTypingStatus = (isTyping: boolean) => {
    socketClient.publish<TypingStatusChangeRequest>(SOCKET_PATHS.TYPING.SEND, {
      chatRoomId: selectedChatRoom.id,
      isTyping,
    });
  };

  useEffect(() => {
    if (input) {
      sendTypingStatus(true);
      const timeout = setTimeout(() => sendTypingStatus(false), 500);
      return () => clearTimeout(timeout);
    } else {
      sendTypingStatus(false);
    }
  }, [input]);

  return (
    <div className="w-full mx-auto p-4 bg-white shadow rounded-lg h-full flex flex-col">
      <h2 className="text-xl font-semibold mb-4">
        채팅방 - {selectedChatRoom.name}
      </h2>
      <MessageList messages={messages} />
      <TypingIndicator typingUsers={typingUsers} />
      <MessageInput input={input} setInput={setInput} onSend={sendMessage} />
    </div>
  );
};

export default ChatRoomView;
