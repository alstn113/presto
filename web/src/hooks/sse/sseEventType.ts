import type { JoinedChatRoomPreviewUpdatedEvent } from '../socket/types';

export const SseEvent = {
  JOINED_CHAT_ROOMS_PREVIEW_UPDATED: 'joined_chat_rooms_preview_updated',
} as const;

export interface SseEventPayloadMap {
  [SseEvent.JOINED_CHAT_ROOMS_PREVIEW_UPDATED]: JoinedChatRoomPreviewUpdatedEvent;
}

export type SseEventType = (typeof SseEvent)[keyof typeof SseEvent];
