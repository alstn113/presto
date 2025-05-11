import { useEffect } from 'react';
import { SOCKET_PATHS } from '../../constants';
import type { TypingStatusChangedEvent } from './types';
import { socketClient } from '../../libs/socket/socketClient';

const useChatTypingStatusSocket = (
  chatRoomId: string,
  onReceived: (event: TypingStatusChangedEvent) => void
) => {
  useEffect(() => {
    socketClient.subscribe<TypingStatusChangedEvent>(
      SOCKET_PATHS.TYPING.SUBSCRIBE(chatRoomId),
      onReceived
    );

    return () => {
      socketClient.unsubscribe(SOCKET_PATHS.TYPING.SUBSCRIBE(chatRoomId));
    };
  }, [chatRoomId]);
};

export default useChatTypingStatusSocket;
