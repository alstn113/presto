import type { MessageType } from '../../hooks/socket/types';
import { apiV1Client } from './apiClient';
import { handleAPIResponse } from './apiUtils';
import type { ApiResponse, CursorResult } from './response/apiResponse';

export const ChatMessageApi = {
  getChatMessages: async ({
    chatRoomId,
    direction,
    cursorMessageId = null,
    lastMessageId = null,
    size = 30,
  }: ChatMessagesRequest) => {
    return await handleAPIResponse(() =>
      apiV1Client.get<ApiResponse<CursorResult<ChatMessageDto>>>(
        `/chat-rooms/${chatRoomId}/messages`,
        {
          params: {
            direction,
            cursorMessageId,
            lastMessageId,
            size,
          },
        }
      )
    );
  },
};

interface ChatMessagesRequestBase {
  chatRoomId: string;
  size?: number;
}

export type ChatMessagesRequest =
  | (ChatMessagesRequestBase & {
      direction: 'INIT';
      lastMessageId: string | null;
      cursorMessageId: null;
    })
  | (ChatMessagesRequestBase & {
      direction: 'NEXT';
      lastMessageId: null;
      cursorMessageId: string;
    })
  | (ChatMessagesRequestBase & {
      direction: 'PREV';
      lastMessageId: null;
      cursorMessageId: string;
    });

export interface ChatMessageDto {
  id: string;
  content: string;
  messageType: MessageType;
  sender: {
    id: string;
    username: string;
  };
  sentAt: string;
}
