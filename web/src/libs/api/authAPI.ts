import { apiV1Client } from './apiClient';
import { handleAPIResponse } from './apiUtils';
import type { ApiResponse } from './response/apiResponse';

export const AuthAPI = {
  login: async (request: LoginRequest) => {
    const response = await handleAPIResponse(() =>
      apiV1Client.post<ApiResponse<void>>('/auth/login', request)
    );

    return response;
  },

  register: async (request: RegisterRequest) => {
    const response = await handleAPIResponse(() =>
      apiV1Client.post<ApiResponse<void>>('/auth/register', request)
    );
    return response;
  },

  logout: async () => {
    const response = await handleAPIResponse(() =>
      apiV1Client.post<ApiResponse<void>>('/auth/logout')
    );
    return response;
  },
};

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
}
