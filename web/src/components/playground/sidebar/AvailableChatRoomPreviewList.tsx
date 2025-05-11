import useGetAvailableChatRoomPreviews from '../../../hooks/chat/useAvailableChatRoomPreviews';
import AvailableChatRoomPreviewItem from './AvailableChatRoomPreviewItem';

const AvailableChatRoomPreviewList = () => {
  const { data: rooms } = useGetAvailableChatRoomPreviews();

  return (
    <div className="mb-6">
      <h3 className="text-lg font-semibold text-gray-600 mb-2">참여 가능</h3>
      <ul className="space-y-2">
        {rooms.length > 0 ? (
          rooms.map((room) => (
            <AvailableChatRoomPreviewItem
              key={room.chatRoomId}
              room={{ id: room.chatRoomId, name: room.chatRoomName }}
            />
          ))
        ) : (
          <p className="text-sm text-gray-400">표시할 채팅방이 없습니다.</p>
        )}
      </ul>
    </div>
  );
};

export default AvailableChatRoomPreviewList;
