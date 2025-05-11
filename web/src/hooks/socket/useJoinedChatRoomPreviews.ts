import { useEffect } from 'react';
import { SOCKET_PATHS } from '../../constants';
import { socketClient } from '../../libs/socket/socketClient';
import type { JoinedChatRoomPreviewUpdatedEvent } from './types';

const useJoinedChatRoomPreviewSocket = (
  onMessageReceived: (event: JoinedChatRoomPreviewUpdatedEvent) => void
) => {
  useEffect(() => {
    socketClient.subscribe<JoinedChatRoomPreviewUpdatedEvent>(
      SOCKET_PATHS.JOINED_CHAT_ROOM_PREVIEW.SUBSCRIBE,
      onMessageReceived
    );

    return () => {
      socketClient.unsubscribe(SOCKET_PATHS.JOINED_CHAT_ROOM_PREVIEW.SUBSCRIBE);
    };
  }, []);
};

export default useJoinedChatRoomPreviewSocket;
