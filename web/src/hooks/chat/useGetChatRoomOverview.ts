import { useSuspenseQuery } from '@tanstack/react-query';
import { ChatRoomApi } from '../../libs/api/chatRoomApi';

interface UseGetChatRoomOverviewProps {
  chatRoomId: string;
}
const useGetChatRoomOverview = ({
  chatRoomId,
}: UseGetChatRoomOverviewProps) => {
  return useSuspenseQuery({
    queryKey: getKey(chatRoomId),
    queryFn: fetcher(chatRoomId),
  });
};

const getKey = (chatRoomId: string) => ['useGetChatRoomOverview', chatRoomId];
const fetcher = (chatRoomId: string) => async () =>
  await ChatRoomApi.getChatRoomOverview({
    chatRoomId,
  });

useGetChatRoomOverview.getKey = getKey;
useGetChatRoomOverview.fetcher = fetcher;

export default useGetChatRoomOverview;
