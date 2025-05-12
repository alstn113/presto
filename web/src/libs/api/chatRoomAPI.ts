import { apiV1Client } from './apiClient';
import { handleAPIResponse } from './apiUtils';
import type { ApiResponse } from './response/apiResponse';

export const ChatRoomAPI = {
  getJoinedChatRoomPreviews: async () => {
    const response = await handleAPIResponse(() =>
      apiV1Client.get<ApiResponse<JoinedChatRoomPreviewResponse[]>>(
        '/chat-rooms/joined'
      )
    );
    return response;
  },

  getAvailableChatRoomPreviews: async () => {
    const response = await handleAPIResponse(() =>
      apiV1Client.get<ApiResponse<AvailableChatRoomPreviewResponse[]>>(
        '/chat-rooms/available'
      )
    );
    return response;
  },

  joinChatRoom: async ({ chatRoomId }: JoinChatRoomRequest) => {
    const response = await handleAPIResponse(() =>
      apiV1Client.post<ApiResponse<void>>(`/chat-rooms/${chatRoomId}/join`)
    );
    return response;
  },

  leaveChatRoom: async ({ chatRoomId }: LeaveChatRoomRequest) => {
    const response = await handleAPIResponse(() =>
      apiV1Client.post<ApiResponse<void>>(`/chat-rooms/${chatRoomId}/leave`)
    );
    return response;
  },
};

export interface JoinedChatRoomPreviewResponse {
  chatRoomId: string;
  chatRoomName: string;
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
