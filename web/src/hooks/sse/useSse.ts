import { useEffect, useRef } from 'react';
import { SSE_URL } from '../socket/socketPaths';
import useSseEventBusStore from '../../store/useSseEventBusStore';
import { SseEvent, type SseEventType } from './sseEventType';

const useSse = () => {
  const eventSourceRef = useRef<EventSource | null>(null);
  const { emit, clear } = useSseEventBusStore();

  const handleAddEventListener = (eventType: SseEventType) => {
    eventSourceRef.current?.addEventListener(eventType, (event) => {
      try {
        const data = JSON.parse(event.data);
        console.log('Event received:', eventType, data);
        emit(eventType, data);
      } catch {
        emit(eventType, event.data);
      }
    });
  };

  useEffect(() => {
    eventSourceRef.current = new EventSource(SSE_URL, {
      withCredentials: true,
    });

    eventSourceRef.current.onopen = () => {
      console.log('SSE connection opened');
    };

    eventSourceRef.current.onmessage = (event) => {
      console.log('SSE event received:', event.data);
    };

    for (const eventType of Object.values(SseEvent)) {
      handleAddEventListener(eventType);
    }

    eventSourceRef.current.onerror = (error) => {
      console.error('SSE error:', error);
    };

    return () => {
      eventSourceRef.current?.close();
      eventSourceRef.current = null;
      clear();
      console.log('SSE connection closed');
    };
  }, []);
};

export default useSse;
