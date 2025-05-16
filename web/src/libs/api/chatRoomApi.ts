import { apiV1Client } from './apiClient';
import { handleAPIResponse } from './apiUtils';
import type { ApiResponse } from './response/apiResponse';

export const ChatRoomApi = {
  getJoinedChatRoomPreviews: async () => {
    return await handleAPIResponse(() =>
      apiV1Client.get<ApiResponse<JoinedChatRoomPreviewResponse[]>>(
        '/chat-rooms/joined'
      )
    );
  },

  getAvailableChatRoomPreviews: async () => {
    return await handleAPIResponse(() =>
      apiV1Client.get<ApiResponse<AvailableChatRoomPreviewResponse[]>>(
        '/chat-rooms/available'
      )
    );
  },

  joinChatRoom: async ({ chatRoomId }: JoinChatRoomRequest) => {
    return await handleAPIResponse(() =>
      apiV1Client.post<ApiResponse<void>>(`/chat-rooms/${chatRoomId}/join`)
    );
  },

  leaveChatRoom: async ({ chatRoomId }: LeaveChatRoomRequest) => {
    return await handleAPIResponse(() =>
      apiV1Client.post<ApiResponse<void>>(`/chat-rooms/${chatRoomId}/leave`)
    );
  },
};

export interface JoinedChatRoomPreviewResponse {
  chatRoomId: string;
  chatRoomName: string;
  chatMessageId: string;
  lastMessageContent: string;
  lastMessageSentAt: string;
  unreadMessageCount: number;
  participantCount: number;
}

export interface AvailableChatRoomPreviewResponse {
  chatRoomId: string;
  chatRoomName: string;
  createdAt: string;
}

export interface JoinChatRoomRequest {
  chatRoomId: string;
}

export interface LeaveChatRoomRequest {
  chatRoomId: string;
}
