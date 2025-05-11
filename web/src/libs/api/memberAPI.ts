import { apiV1Client } from './apiClient';
import type { ApiResponse } from './response/apiResponse';

export const MemberAPI = {
  getMyInfo: async () => {
    const response = await apiV1Client.get<ApiResponse<MemberInfoResponse>>(
      '/members/me'
    );
    return response.data;
  },
};

export interface MemberInfoResponse {
  id: number;
  username: string;
  createdAt: string;
}
