import { BASE_URL } from '../../constants';

export const SOCKET_URL: string = `${BASE_URL}/ws`;

export const SOCKET_PATHS = {
  CHAT: {
    SEND: '/app/chat.send',
    SUBSCRIBE: (chatRoomId: string) => `/topic/chat/${chatRoomId}`,
  },
  TYPING: {
    SEND: '/app/chat.typing',
    SUBSCRIBE: (chatRoomId: string) => `/topic/chat/${chatRoomId}/typing`,
  },
  JOINED_CHAT_ROOM_PREVIEW: {
    SUBSCRIBE: `/user/queue/chat-rooms/joined`,
  },
};
