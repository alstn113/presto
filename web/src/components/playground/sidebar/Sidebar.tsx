import JoinedChatRoomPreviewList from './JoinedChatRoomPreviewList';
import AvailableChatRoomPreviewList from './AvailableChatRoomPreviewList';
import { ErrorBoundary } from 'react-error-boundary';
import { Suspense } from 'react';

const Sidebar = () => {
  return (
    <aside className="w-2/7 bg-white border-r border-gray-200 shadow-sm p-6">
      <h2 className="text-2xl font-bold mb-6 text-gray-800">채팅방</h2>
      <ErrorBoundary
        FallbackComponent={() => (
          <div>채팅방 목록을 불러오는 중 오류가 발생했습니다.</div>
        )}
      >
        <Suspense fallback={<div>Loading...</div>}>
          <JoinedChatRoomPreviewList />
        </Suspense>
      </ErrorBoundary>
      <ErrorBoundary
        FallbackComponent={() => (
          <div>채팅방 목록을 불러오는 중 오류가 발생했습니다.</div>
        )}
      >
        <Suspense fallback={<div>Loading...</div>}>
          <AvailableChatRoomPreviewList />
        </Suspense>
      </ErrorBoundary>
    </aside>
  );
};

export default Sidebar;
