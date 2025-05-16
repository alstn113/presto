import type { JoinedChatRoomPreviewResponse } from '../../../libs/api/chatRoomApi';
import { formatRelativeDate } from '../../../libs/utils/dateUtils';
import useChatRoomStore from '../../../store/useChatRoomStore';

interface ChatRoomPreviewProps {
  room: JoinedChatRoomPreviewResponse;
}

const JoinedChatRoomPreviewItem = ({ room }: ChatRoomPreviewProps) => {
  const { selectedChatRoom, selectChatRoom } = useChatRoomStore();

  const isSelected = selectedChatRoom?.id === room.chatRoomId;

  return (
    <li
      key={room.chatRoomId}
      className={`flex justify-between items-center gap-4 px-4 py-3 rounded-2xl cursor-pointer transition-colors
        ${
          isSelected
            ? 'bg-blue-100 text-blue-800 font-semibold'
            : 'hover:bg-gray-100 text-gray-800'
        }`}
      onClick={() =>
        selectChatRoom({ id: room.chatRoomId, name: room.chatRoomName })
      }
    >
      <div className="flex flex-col flex-1 min-w-0">
        <div className="flex items-center gap-2">
          <span className="truncate font-medium text-base">
            {room.chatRoomName}
          </span>
          <span className="text-xs text-gray-400">
            {room.participantCount}명
          </span>
        </div>
        <div className="text-sm text-gray-500 truncate">
          {room.lastMessageContent}
        </div>
      </div>

      <div className="flex flex-col items-end gap-1">
        <span className="text-xs text-gray-400 whitespace-nowrap">
          {room.lastMessageSentAt && formatRelativeDate(room.lastMessageSentAt)}
        </span>

        {room.unreadMessageCount > 0 && (
          <span className="bg-red-500 text-white text-xs font-bold px-2 py-0.5 rounded-full">
            {room.unreadMessageCount}
          </span>
        )}
      </div>
    </li>
  );
};

export default JoinedChatRoomPreviewItem;
