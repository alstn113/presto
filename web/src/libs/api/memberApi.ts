import {apiV1Client} from './apiClient';
import {handleAPIResponse} from './apiUtils';
import type {ApiResponse} from './response/apiResponse';

export const MemberApi = {
  getMyInfo: async () => {
    return await handleAPIResponse(() =>
        apiV1Client.get<ApiResponse<MemberInfoResponse>>('/members/me')
    );
  },
};

export interface MemberInfoResponse {
  id: string;
  username: string;
  createdAt: string;
}
