import type { ErrorMessage } from '../error/errorMessage';
import type { ResultType } from './resultType';

export type ApiResponse<T> = SuccessResponse<T> | ErrorResponse;

export interface SuccessResponse<T> {
  result: typeof ResultType.SUCCESS;
  data: T;
  error: null;
  timestamp: string;
}

export interface ErrorResponse {
  result: typeof ResultType.ERROR;
  data: null;
  error: ErrorMessage;
  timestamp: string;
}
