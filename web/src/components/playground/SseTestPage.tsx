import { useEffect, useState } from 'react';
import { apiV1Client } from '../../libs/api/apiClient';
import useSseEventBusStore from '../../store/useSseEventBusStore';
import { SseEvent } from '../../hooks/sse/sseEventType';
import useSse from '../../hooks/sse/useSse';

const SseTestPage = () => {
  useSse();

  const [showTestA, setShowTestA] = useState(true);
  const [showTestB, setShowTestB] = useState(true);

  const handleActiveNotifications = async () => {
    await apiV1Client.get('/sse/active', {
      withCredentials: true,
    });
  };

  return (
    <div>
      <h1 className="text-2xl font-bold">SSE Test Page</h1>
      <button
        onClick={handleActiveNotifications}
        className="px-4 py-2 mt-4 text-white bg-blue-500 rounded"
      >
        Fetch Active Notifications
      </button>

      <div className="mt-4 space-x-4">
        <button
          onClick={() => setShowTestA((prev) => !prev)}
          className="px-4 py-2 text-white bg-green-500 rounded"
        >
          Toggle TestA
        </button>
        <button
          onClick={() => setShowTestB((prev) => !prev)}
          className="px-4 py-2 text-white bg-purple-500 rounded"
        >
          Toggle TestB
        </button>
      </div>

      <div className="mt-4">
        <h2 className="text-xl font-semibold">Test Components</h2>
        <div className="flex space-x-4">
          <div className="w-1/2">{showTestA && <TestA />}</div>
          <div className="w-1/2">{showTestB && <TestB />}</div>
        </div>
      </div>
    </div>
  );
};

const TestA = () => {
  const [messages, setMessages] = useState<string[]>([]);
  const { on, off } = useSseEventBusStore();

  useEffect(() => {
    const handleMessageReceived = (message: string) => {
      setMessages((prev) => [...prev, `${message} + A`]);
    };

    on(SseEvent.TEST, handleMessageReceived);
    return () => off(SseEvent.TEST, handleMessageReceived);
  }, []);

  return (
    <div>
      <div className="font-bold">TestA</div>
      {messages.map((message, index) => (
        <div key={index} className="border-b py-2">
          {message}
        </div>
      ))}
    </div>
  );
};

const TestB = () => {
  const [messages, setMessages] = useState<string[]>([]);
  const { on, off } = useSseEventBusStore();

  useEffect(() => {
    const handleMessageReceived = (message: string) => {
      setMessages((prev) => [...prev, `${message} + B`]);
    };

    on(SseEvent.TEST, handleMessageReceived);
    return () => off(SseEvent.TEST, handleMessageReceived);
  }, []);

  return (
    <div>
      <div className="font-bold">TestB</div>
      {messages.map((message, index) => (
        <div key={index} className="border-b py-2">
          {message}
        </div>
      ))}
    </div>
  );
};

export default SseTestPage;
