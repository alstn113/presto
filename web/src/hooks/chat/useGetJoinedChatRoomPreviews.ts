import { useSuspenseQuery } from '@tanstack/react-query';
import { ChatRoomApi } from '../../libs/api/chatRoomApi';

const useGetJoinedChatRoomPreviews = () => {
  return useSuspenseQuery({
    queryKey: getKey(),
    queryFn: fetcher(),
  });
};

const getKey = () => ['useGetJoinedChatRoomPreviews'];
const fetcher = () => async () => await ChatRoomApi.getJoinedChatRoomPreviews();

useGetJoinedChatRoomPreviews.getkey = getKey;
useGetJoinedChatRoomPreviews.fetcher = fetcher;

export default useGetJoinedChatRoomPreviews;
