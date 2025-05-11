import { useEffect } from 'react';
import { SOCKET_PATHS } from '../../constants';
import type { ChatMessageReceivedEvent } from './types';
import { socketClient } from '../../libs/socket/socketClient';

const useChatMessageSocket = (
  chatRoomId: string,
  onReceived: (event: ChatMessageReceivedEvent) => void
) => {
  useEffect(() => {
    socketClient.subscribe(SOCKET_PATHS.CHAT.SUBSCRIBE(chatRoomId), onReceived);

    return () => {
      socketClient.unsubscribe(SOCKET_PATHS.CHAT.SUBSCRIBE(chatRoomId));
    };
  }, [chatRoomId]);
};

export default useChatMessageSocket;
