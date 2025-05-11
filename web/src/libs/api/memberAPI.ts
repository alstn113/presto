import { apiV1Client } from './apiClient';
import { handleAPIResponse } from './apiUtils';
import type { ApiResponse } from './response/apiResponse';

export const MemberAPI = {
  getMyInfo: async () => {
    const response = await handleAPIResponse(() =>
      apiV1Client.get<ApiResponse<MemberInfoResponse>>('/members/me')
    );
    return response;
  },
};

export interface MemberInfoResponse {
  id: string;
  username: string;
  createdAt: string;
}
