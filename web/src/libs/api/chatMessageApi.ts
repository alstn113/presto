import type { MessageType } from '../../hooks/socket/types';
import { apiV1Client } from './apiClient';
import { handleAPIResponse } from './apiUtils';
import type { ApiResponse, CursorResult } from './response/apiResponse';

export const ChatMessageApi = {
  getChatMessages: async ({
    chatRoomId,
    direction,
    cursorMessageId = null,
    size = 30,
  }: ChatMessagesRequest) => {
    return await handleAPIResponse(() =>
      apiV1Client.get<ApiResponse<CursorResult<ChatMessageDto>>>(
        `/chat-rooms/${chatRoomId}/messages`,
        {
          params: {
            direction,
            cursorMessageId,
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
      cursorMessageId: null;
    })
  | (ChatMessagesRequestBase & {
      direction: 'NEXT';
      cursorMessageId: string;
    })
  | (ChatMessagesRequestBase & {
      direction: 'PREV';
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
