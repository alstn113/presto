import { useState } from 'react';
import { apiV1Client } from '../../libs/api/apiClient';
import useSse from '../../hooks/sse/useSse';
import useUpdateEffect from '../../hooks/useUpdateEffect';

const SseTestPage = () => {
  const [messages, setMessages] = useState<string[]>([]);
  const { test } = useSse();

  useUpdateEffect(() => {
    console.log('Test event received in use updated Effect:', test);
    if (test) {
      setMessages((prevMessages) => [...prevMessages, test.event]);
    }
  }, [test]);

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
      <div className="mt-4">
        <h2 className="text-xl font-semibold">Messages:</h2>
        <ul>
          {messages.map((message, index) => (
            <li key={index} className="border-b py-2">
              {message}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default SseTestPage;
