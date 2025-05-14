import { useEffect } from 'react';
import useSseEventBusStore from '../../store/useSseEventBusStore';
import type { SseEventPayloadMap, SseEventType } from './sseEventType';

const useSseListener = <K extends SseEventType>(
  eventType: K,
  callback: (payload: SseEventPayloadMap[K]) => void
) => {
  const { on, off } = useSseEventBusStore();

  useEffect(() => {
    on(eventType, callback);

    return () => {
      off(eventType, callback);
    };
  }, [eventType]);
};

export default useSseListener;
