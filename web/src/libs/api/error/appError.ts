import type { ErrorCode, ErrorMessage } from './errorMessage';

class AppError extends Error {
  private error: ErrorMessage;
  private status: number;

  constructor(error: ErrorMessage, status: number) {
    super(error.message);
    this.name = 'AppError';
    this.error = error;
    this.status = status;
  }

  getErrorMessage(): string {
    return this.error.message;
  }

  getErrorCode(): ErrorCode {
    return this.error.code;
  }

  getErrorData(): unknown | null {
    return this.error.data;
  }

  getStatus(): number {
    return this.status;
  }
}

export default AppError;
