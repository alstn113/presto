export const MessageType = {
  TEXT: 'TEXT',
  SYSTEM: 'SYSTEM',
} as const;
export type MessageType = (typeof MessageType)[keyof typeof MessageType];

export interface ChatMessageSendRequest {
  chatRoomId: string;
  content: string;
}

export interface TypingStatusChangeRequest {
  chatRoomId: string;
  isTyping: boolean;
}

export interface ChatMessageReceivedEvent {
  id: string;
  content: string;
  messageType: MessageType;
  senderId: string;
  sentAt: string;
}

export interface JoinedChatRoomPreviewUpdatedEvent {
  chatRoomId: string;
  chatRoomName: string;
  chatMessageId: string;
  lastMessageContent: string;
  lastMessageSentAt: string;
  unreadMessageCount: number;
  participantCount: number;
}

export interface TypingStatusChangedEvent {
  chatRoomId: string;
  senderId: string;
  isTyping: boolean;
}
