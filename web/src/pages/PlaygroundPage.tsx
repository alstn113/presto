import { useEffect } from 'react';
import Navbar from '../components/Navbar';
import { socketClient } from '../libs/socket/socketClient';
import Sidebar from '../components/playground/sidebar/Sidebar';
import useChatRoomStore from '../store/useChatRoomStore';
import ChatRoomView from '../components/playground/chatroom/ChatRoomView';

const PlaygroundPage = () => {
  const { selectedChatRoom } = useChatRoomStore();

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
          <ChatRoomView selectedChatRoom={selectedChatRoom} />
        )}{' '}
      </div>
    </>
  );
};

export default PlaygroundPage;
