import type { MessageType } from '../../hooks/socket/types';
import { apiV1Client } from './apiClient';
import { handleAPIResponse } from './apiUtils';
import type { ApiResponse } from './response/apiResponse';

export const ChatMessageApi = {
  getChatMessages: async ({
    chatRoomId,
    direction,
    cursorMessageId,
    size = 30,
  }: ChatMessagesCursorRequest) => {
    return await handleAPIResponse(() =>
      apiV1Client.get<ApiResponse<ChatMessagesCursorResponse>>(
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

export interface ChatMessagesCursorRequest {
  chatRoomId: string;
  size?: number;
  direction: 'NEXT' | 'PREV';
  cursorMessageId: string;
}

export interface ChatMessagesCursorResponse {
  messages: ChatMessageDto[];
  prevCursor: string;
  nextCursor: string;
}

export interface ChatMessageDto {
  id: string;
  content: string;
  messageType: MessageType;
  senderId: string;
  sentAt: string;
}
