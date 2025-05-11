import { AxiosError, type AxiosResponse } from 'axios';
import type { ApiResponse } from './response/apiResponse';
import AppError from './error/appError';
import type { ErrorMessage } from './error/errorMessage';

export const handleAPIResponse = async <T>(
  apiCall: () => Promise<AxiosResponse<ApiResponse<T>>>
): Promise<T> => {
  try {
    const response = await apiCall();
    if (response.data.result === 'SUCCESS') {
      return response.data.data;
    }

    throw new AppError(response.data.error, response.status);
  } catch (e) {
    if (e instanceof AxiosError) {
      const status: number = e.response?.status || 500;
      const errorMessage: ErrorMessage = e.response?.data?.error || null;

      throw new AppError(errorMessage || UNKNOWN_ERROR, status);
    }
  }

  throw new AppError(UNKNOWN_ERROR, 500);
};

const UNKNOWN_ERROR = {
  code: 'UNKNOWN_ERROR',
  message: '알 수 없는 오류가 발생했습니다.',
  data: null,
} as ErrorMessage;
