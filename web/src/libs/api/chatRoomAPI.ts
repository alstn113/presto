import { apiV1Client } from './apiClient';
import type { ApiResponse } from './response/apiResponse';

export const ChatRoomAPI = {
  getJoinedChatRoomPreviews: async () => {
    const response = await apiV1Client.get<
      ApiResponse<JoinedChatRoomPreviewResponse[]>
    >('/chat-rooms/joined');
    return response.data;
  },

  getAvailableChatRoomPreviews: async () => {
    const response = await apiV1Client.get<
      ApiResponse<AvailableChatRoomPreviewResponse[]>
    >('/chat-rooms/available');
    return response.data;
  },

  joinChatRoom: async ({ chatRoomId }: JoinChatRoomRequest) => {
    const response = await apiV1Client.post<ApiResponse<void>>(
      `/chat-rooms/${chatRoomId}/join`
    );
    return response.data;
  },

  leaveChatRoom: async ({ chatRoomId }: LeaveChatRoomRequest) => {
    const response = await apiV1Client.post<ApiResponse<void>>(
      `/chat-rooms/${chatRoomId}/leave`
    );
    return response.data;
  },
};

export interface JoinedChatRoomPreviewResponse {
  chatRoomId: number;
  chatRoomName: string;
  lastMessageContent: string;
  lastMessageSentAt: string;
}

export interface AvailableChatRoomPreviewResponse {
  chatRoomId: number;
  chatRoomName: string;
  createdAt: string;
}

export interface JoinChatRoomRequest {
  chatRoomId: string;
}

export interface LeaveChatRoomRequest {
  chatRoomId: string;
}
