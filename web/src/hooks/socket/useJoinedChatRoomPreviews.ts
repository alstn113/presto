import { SOCKET_PATHS } from './socketPaths';
import type {
  JoinedChatRoomPreviewUpdatedEvent,
  TypingStatusChangeRequest,
} from './types';
import useSocket from './useSocket';

const useJoinedChatRoomPreviewSocket = <T = JoinedChatRoomPreviewUpdatedEvent>(
  onReceived: (event: T) => void
) => {
  useSocket<TypingStatusChangeRequest, T>(
    '',
    SOCKET_PATHS.JOINED_CHAT_ROOM_PREVIEW.SUBSCRIBE,
    onReceived
  );
};

export default useJoinedChatRoomPreviewSocket;
