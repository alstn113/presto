import { useEffect, useRef, useState } from 'react';
import { SSE_URL } from '../socket/socketPaths';
import { SseEventType } from './sseEventType';
import useSseStore from '../../store/useSseStore';

const useSse = () => {
  const [isConnected, setIsConnected] = useState<boolean>(false);
  const eventSourceRef = useRef<EventSource | null>(null);
  const { joinedChatRoomPreviewUpdated, setJoinedChatRoomPreviewUpdated } =
    useSseStore();

  const handleAddEventListener = <T>(
    eventType: SseEventType,
    callback: (event: T) => void
  ) => {
    eventSourceRef.current?.addEventListener(eventType, (event) => {
      try {
        const data = JSON.parse(event.data) as T;
        callback(data);
      } catch (error) {
        console.error('Error parsing SSE data:', error);
      }
    });
  };

  useEffect(() => {
    eventSourceRef.current = new EventSource(SSE_URL, {
      withCredentials: true,
    });

    eventSourceRef.current.onopen = () => {
      setIsConnected(true);
      console.log('SSE connection opened');
    };

    eventSourceRef.current.onmessage = (event) => {
      console.log('SSE event received:', event.data);
    };

    handleAddEventListener(
      SseEventType.JOINED_CHAT_ROOMS_PREVIEW_UPDATED,
      setJoinedChatRoomPreviewUpdated
    );

    eventSourceRef.current.onerror = (error) => {
      console.error('SSE error:', error);
    };

    return () => {
      eventSourceRef.current?.close();
      eventSourceRef.current = null;
      setIsConnected(false);
      console.log('SSE connection closed');
    };
  }, []);

  return {
    isConnected,
    joinedChatRoomPreviewUpdated,
  };
};

export default useSse;
