import {apiV1Client} from './apiClient';
import {handleAPIResponse} from './apiUtils';
import type {ApiResponse} from './response/apiResponse';

export const AuthApi = {
  login: async (request: LoginRequest) => {
    return await handleAPIResponse(() =>
        apiV1Client.post<ApiResponse<void>>('/auth/login', request)
    );
  },

  register: async (request: RegisterRequest) => {
    return await handleAPIResponse(() =>
        apiV1Client.post<ApiResponse<void>>('/auth/register', request)
    );
  },

  logout: async () => {
    return await handleAPIResponse(() =>
        apiV1Client.post<ApiResponse<void>>('/auth/logout')
    );
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
