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

const createInitialListeners = () => {
  const entries = Object.entries(SseEvent) as [SseEventType, string][];
  const result = {} as States['listeners'];

  for (const [key] of entries) {
    result[key] = new Set<EventCallback<SseEventPayloadMap[typeof key]>>();
  }

  return result;
};

const useSseEventBusStore = create<States & Actions>()(
  immer((set, get) => ({
    listeners: createInitialListeners(),
    on: <K extends SseEventType>(
      event: K,
      callback: EventCallback<SseEventPayloadMap[K]>
    ) => {
      const listeners = get().listeners;
      const nextSet = new Set(listeners[event]);
      nextSet.add(callback);

      set({ listeners: { ...listeners, [event]: nextSet } });
    },

    off: <K extends SseEventType>(
      event: K,
      callback: EventCallback<SseEventPayloadMap[K]>
    ) => {
      const listeners = get().listeners;
      const nextSet = new Set(listeners[event]);
      nextSet.delete(callback);

      set({ listeners: { ...listeners, [event]: nextSet } });
    },

    emit: <K extends SseEventType>(
      event: K,
      payload: SseEventPayloadMap[K]
    ) => {
      const listeners = get().listeners[event];
      [...listeners].forEach((callback) => {
        try {
          callback(payload);
        } catch (error) {
          console.error(`Error in event handler for ${event}:`, error);
        }
      });
    },

    clear: () => {
      set({ listeners: createInitialListeners() });
    },
  }))
);

export default useSseEventBusStore;
