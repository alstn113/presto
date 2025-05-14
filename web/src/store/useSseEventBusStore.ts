import { create } from 'zustand';
import { immer } from 'zustand/middleware/immer';
import {
  SseEvent,
  type SseEventPayloadMap,
  type SseEventType,
} from '../hooks/sse/sseEventType';

type EventCallback<T> = (payload: T) => void;

type States = {
  listeners: {
    [K in SseEventType]: Set<EventCallback<SseEventPayloadMap[K]>>;
  };
};

type Actions = {
  on: <K extends SseEventType>(
    eventType: K,
    callback: EventCallback<SseEventPayloadMap[K]>
  ) => void;

  off: <K extends SseEventType>(
    eventType: K,
    callback: EventCallback<SseEventPayloadMap[K]>
  ) => void;

  emit: <K extends SseEventType>(
    eventType: K,
    payload: SseEventPayloadMap[K]
  ) => void;

  clear: () => void;
};

const initialState: States = {
  listeners: {
    [SseEvent.JOINED_CHAT_ROOMS_PREVIEW_UPDATED]: new Set(),
    [SseEvent.TEST]: new Set(),
  },
};

const useSseEventBusStore = create<States & Actions>()(
  immer((set, get) => ({
    ...initialState,

    on: <K extends SseEventType>(
      event: K,
      callback: EventCallback<SseEventPayloadMap[K]>
    ) => {
      const listeners = get().listeners;
      listeners[event].add(callback);
      set({ listeners });
    },

    off: <K extends SseEventType>(
      event: K,
      callback: EventCallback<SseEventPayloadMap[K]>
    ) => {
      const listeners = get().listeners;
      listeners[event].delete(callback);
      set({ listeners });
    },

    emit: <K extends SseEventType>(
      event: K,
      payload: SseEventPayloadMap[K]
    ) => {
      const listeners = get().listeners[event];
      listeners.forEach((callback) => {
        callback(payload);
      });
    },

    clear: () => {
      set({ listeners: initialState.listeners });
    },
  }))
);

export default useSseEventBusStore;
