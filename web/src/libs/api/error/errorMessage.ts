export interface ErrorMessage {
  code: ErrorCode;
  message: string;
  data: unknown | null;
}

export const ErrorCode = {
  UNKNOWN_ERROR: 'UNKNOWN_ERROR',

  INTERNAL_SERVER_ERROR: 'INTERNAL_SERVER_ERROR',
  UNAUTHORIZED: 'UNAUTHORIZED',
  FORBIDDEN: 'FORBIDDEN',

  MEMBER_NOT_FOUND: 'MEMBER_NOT_FOUND',
  MEMBER_USERNAME_ALREADY_EXISTS: 'MEMBER_USERNAME_ALREADY_EXISTS',
  MEMBER_PASSWORD_MISMATCH: 'MEMBER_PASSWORD_MISMATCH',

  CHAT_ROOM_NOT_FOUND: 'CHAT_ROOM_NOT_FOUND',
  CHAT_ROOM_PARTICIPANT_NOT_FOUND: 'CHAT_ROOM_PARTICIPANT_NOT_FOUND',
} as const;
export type ErrorCode = (typeof ErrorCode)[keyof typeof ErrorCode];
