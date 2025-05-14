export const SseEventType = {
  JOINED_CHAT_ROOMS_PREVIEW_UPDATED: 'joined-chat-rooms-preview-updated',
} as const;

export type SseEventType = (typeof SseEventType)[keyof typeof SseEventType];
