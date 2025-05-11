import { useMutation, useQueryClient } from '@tanstack/react-query';
import useChatRoomStore from '../../../store/useChatRoomStore';
import { ChatRoomAPI } from '../../../libs/api/chatRoomAPI';
import useGetJoinedChatRoomPreviews from '../../../hooks/chat/useGetJoinedChatRoomPreviews';
import useGetAvailableChatRoomPreviews from '../../../hooks/chat/useAvailableChatRoomPreviews';

interface AvailableChatRoomItemProps {
  room: { id: string; name: string };
}

const AvailableChatRoomPreviewItem = ({ room }: AvailableChatRoomItemProps) => {
  const queryClient = useQueryClient();
  const { selectedChatRoom, selectChatRoom } = useChatRoomStore();
  const { mutate: joinChatRoom } = useMutation({
    mutationFn: ChatRoomAPI.joinChatRoom,
    onSuccess: async () => {
      await queryClient.invalidateQueries({
        queryKey: useGetJoinedChatRoomPreviews.getkey(),
        refetchType: 'all',
      });
      await queryClient.invalidateQueries({
        queryKey: useGetAvailableChatRoomPreviews.getkey(),
        refetchType: 'all',
      });
      selectChatRoom({ id: room.id, name: room.name });
    },
  });
  return (
    <li
      key={room.id}
      className={`flex items-center gap-3 p-3 rounded-lg cursor-pointer transition hover:bg-gray-100 ${
        selectedChatRoom?.id === room.id
          ? 'bg-blue-100 text-blue-800 font-semibold'
          : 'text-gray-700'
      }`}
      onClick={() => {
        console.log('joinChatRoom', room.id);
        joinChatRoom({ chatRoomId: room.id });
      }}
    >
      <div className={`w-2 h-2 rounded-full bg-green-500`} />
      {room.name}
    </li>
  );
};

export default AvailableChatRoomPreviewItem;
