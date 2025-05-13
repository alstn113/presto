import { SOCKET_PATHS } from './socketPaths';
import type {
  TypingStatusChangedEvent,
  TypingStatusChangeRequest,
} from './types';
import useSocket from './useSocket';

const useChatMessageSocket = <T = TypingStatusChangedEvent>(
  chatRoomId: string,
  onReceived: (event: T) => void
) => {
  const { send } = useSocket<TypingStatusChangeRequest, T>(
    SOCKET_PATHS.TYPING.SEND,
    SOCKET_PATHS.TYPING.SUBSCRIBE(chatRoomId),
    onReceived
  );

  return { sendTypingStatus: send };
};

export default useChatMessageSocket;
