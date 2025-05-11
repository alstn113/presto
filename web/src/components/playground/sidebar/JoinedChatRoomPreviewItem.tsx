import type { JoinedChatRoomPreviewResponse } from '../../../libs/api/chatRoomAPI';
import { formatRelativeDate } from '../../../libs/utils/dateUtils';
import useChatRoomStore from '../../../store/useChatRoomStore';

interface ChatRoomPreviewProps {
  room: JoinedChatRoomPreviewResponse;
}

const JoinedChatRoomPreviewItem = ({ room }: ChatRoomPreviewProps) => {
  const { selectedChatRoom, selectChatRoom } = useChatRoomStore();

  return (
    <li
      key={room.chatRoomId}
      className={`flex justify-between items-center gap-3 p-3 rounded-lg cursor-pointer transition hover:bg-gray-100 ${
        selectedChatRoom?.id === room.chatRoomId
          ? 'bg-blue-100 text-blue-800 font-semibold'
          : 'text-gray-700'
      }`}
      onClick={() =>
        selectChatRoom({ id: room.chatRoomId, name: room.chatRoomName })
      }
    >
      <div className="flex flex-col">
        <span>{room.chatRoomName}</span>
        <span className="text-sm text-gray-500">{room.lastMessageContent}</span>
      </div>
      <span className="text-xs text-gray-400 whitespace-nowrap ml-2">
        {room.lastMessageSentAt && formatRelativeDate(room.lastMessageSentAt)}
      </span>
    </li>
  );
};

export default JoinedChatRoomPreviewItem;
