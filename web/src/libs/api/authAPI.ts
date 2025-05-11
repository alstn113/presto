import { apiV1Client } from './apiClient';
import type { ApiResponse } from './response/apiResponse';

export const AuthAPI = {
  login: async (request: LoginRequest) => {
    const response = await apiV1Client.post<ApiResponse<void>>(
      '/auth/login',
      request
    );
    return response.data;
  },

  register: async (request: RegisterRequest) => {
    const response = await apiV1Client.post<ApiResponse<void>>(
      '/auth/register',
      request
    );
    return response.data;
  },

  logout: async () => {
    const response = await apiV1Client.post<ApiResponse<void>>('/auth/logout');
    return response.data;
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
