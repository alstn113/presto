import { useEffect, useState } from 'react';
import type { JoinedChatRoomPreviewUpdatedEvent } from '../../../hooks/socket/types';
import useJoinedChatRoomPreviewSocket from '../../../hooks/socket/useJoinedChatRoomPreviews';
import useGetJoinedChatRoomPreviews from '../../../hooks/chat/useGetJoinedChatRoomPreviews';
import type { JoinedChatRoomPreviewResponse } from '../../../libs/api/chatRoomAPI';
import JoinedChatRoomPreviewItem from './JoinedChatRoomPreviewItem';

const JoinedChatRoomPreviewList = () => {
  const { data: initialRooms } = useGetJoinedChatRoomPreviews();
  const [rooms, setRooms] = useState<JoinedChatRoomPreviewResponse[]>([]);

  useEffect(() => {
    setRooms(initialRooms);
  }, [initialRooms]);

  const handleOnMessageReceived = (
    message: JoinedChatRoomPreviewUpdatedEvent
  ) => {
    setRooms((prevRooms) => {
      const updatedRooms = prevRooms.filter(
        (room) => room.chatRoomId !== message.chatRoomId
      );
      updatedRooms.push(message);
      return updatedRooms.sort(sortByLastMessageSentAt);
    });
  };

  const sortByLastMessageSentAt = (
    a: JoinedChatRoomPreviewResponse,
    b: JoinedChatRoomPreviewResponse
  ) => {
    return (
      new Date(b.lastMessageSentAt).getTime() -
      new Date(a.lastMessageSentAt).getTime()
    );
  };

  useJoinedChatRoomPreviewSocket(handleOnMessageReceived);

  return (
    <div className="mb-6">
      <h3 className="text-lg font-semibold text-gray-600 mb-2">참여 중</h3>
      <ul className="space-y-2">
        {rooms.length > 0 ? (
          rooms.map((room) => (
            <JoinedChatRoomPreviewItem key={room.chatRoomId} room={room} />
          ))
        ) : (
          <p className="text-sm text-gray-400">표시할 채팅방이 없습니다.</p>
        )}
      </ul>
    </div>
  );
};

export default JoinedChatRoomPreviewList;
