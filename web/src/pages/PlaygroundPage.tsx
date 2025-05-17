import { Suspense, useEffect } from 'react';
import Navbar from '../components/Navbar';
import { socketClient } from '../libs/socket/socketClient';
import Sidebar from '../components/playground/sidebar/Sidebar';
import useChatRoomStore from '../store/useChatRoomStore';
import ChatRoomView from '../components/playground/chatroom/ChatRoomView';
import useSse from '../hooks/sse/useSse';

const PlaygroundPage = () => {
  const { selectedChatRoom } = useChatRoomStore();

  useSse();
  useEffect(() => {
    socketClient.connect();

    return () => {
      socketClient.disconnect();
    };
  }, []);

  return (
    <>
      <Navbar />
      <div className="flex h-screen">
        <Sidebar />
        {!selectedChatRoom ? (
          <div className="flex items-center justify-center w-full h-full text-gray-500">
            채팅방을 선택하세요
          </div>
        ) : (
          <Suspense fallback={<CenteredSpinner />}>
            <ChatRoomView selectedChatRoom={selectedChatRoom} />
          </Suspense>
        )}
      </div>
    </>
  );
};

const CenteredSpinner = () => (
  <div className="flex items-center justify-center w-full h-full">
    <div className="w-12 h-12 border-4 border-blue-500 border-t-transparent border-solid rounded-full animate-spin"></div>
  </div>
);

export default PlaygroundPage;
