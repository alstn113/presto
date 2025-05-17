import { useQueryClient } from '@tanstack/react-query';
import useChatRoomStore from '../../store/useChatRoomStore';
import useGetChatRoomOverview from './useGetChatRoomOverview';
import type {
  ChatRoomOverviewResponse,
  ChatParticipantInfoDto,
} from '../../libs/api/chatRoomApi';

const useChatRoomParticipantInfo = () => {
  const { selectedChatRoom } = useChatRoomStore();
  const chatRoomId = selectedChatRoom?.id;
  const queryClient = useQueryClient();

  const chatRoomOverview: ChatRoomOverviewResponse | undefined = chatRoomId
    ? queryClient.getQueryData(useGetChatRoomOverview.getKey(chatRoomId))
    : undefined;

  const participants = chatRoomOverview?.participants ?? [];

  const getParticipantById = (
    id: string
  ): ChatParticipantInfoDto | undefined => {
    return participants.find((p) => p.id === id);
  };

  const countUnreadParticipants = (currentMessageId: string): number => {
    return participants.reduce((count, participant) => {
      const lastReadMessageId = participant.lastReadMessageId;
      if (!lastReadMessageId || lastReadMessageId < currentMessageId) {
        return count + 1;
      }
      return count;
    }, 0);
  };

  return { participants, getParticipantById, countUnreadParticipants };
};

export default useChatRoomParticipantInfo;
