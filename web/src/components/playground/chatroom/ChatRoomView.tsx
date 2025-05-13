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

interface ChatRoomViewProps {
  selectedChatRoom: { id: string; name: string };
}

const ChatRoomView = ({ selectedChatRoom }: ChatRoomViewProps) => {
  const [messages, setMessages] = useState<ChatMessageReceivedEvent[]>([]);
  const [input, setInput] = useState('');
  const [typingUsers, setTypingUsers] = useState<Record<string, string>>({});

  const handleSendChatMessage = (message: ChatMessageReceivedEvent) => {
    setMessages((prev) => [...prev, message]);
  };
  const handleTypingStatus = ({
    isTyping,
    sender,
  }: TypingStatusChangedEvent) => {
    const { senderId, username } = sender;

    if (isTyping) {
      setTypingUsers((prev) => ({
        ...prev,
        [senderId]: username,
      }));
      return;
    }

    setTypingUsers((prev) => {
      const updated = { ...prev };
      delete updated[senderId];
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

  useEffect(() => {
    setMessages([]);
    setTypingUsers({});
    setInput('');
  }, [selectedChatRoom]);

  useUpdateEffect(() => {
    if (input) {
      sendTypingStatus({ chatRoomId: selectedChatRoom.id, isTyping: true });
      const timeout = setTimeout(
        () =>
          sendTypingStatus({
            chatRoomId: selectedChatRoom.id,
            isTyping: false,
          }),
        500
      );
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
      <TypingIndicator typingUsers={typingUsers} />
      <MessageInput input={input} setInput={setInput} onSend={sendMessage} />
    </div>
  );
};

export default ChatRoomView;
