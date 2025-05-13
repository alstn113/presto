import { SOCKET_PATHS } from './socketPaths';
import type { ChatMessageReceivedEvent, ChatMessageSendRequest } from './types';
import useSocket from './useSocket';

const useChatMessageSocket = <T = ChatMessageReceivedEvent>(
  chatRoomId: string,
  onReceived: (event: T) => void
) => {
  const { send } = useSocket<ChatMessageSendRequest, T>(
    SOCKET_PATHS.CHAT.SEND,
    SOCKET_PATHS.CHAT.SUBSCRIBE(chatRoomId),
    onReceived
  );

  return { sendChatMessage: send };
};

export default useChatMessageSocket;
