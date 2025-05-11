import { useSuspenseQuery } from '@tanstack/react-query';
import { ChatRoomAPI } from '../../libs/api/chatRoomAPI';

const useGetJoinedChatRoomPreviews = () => {
  return useSuspenseQuery({
    queryKey: getKey(),
    queryFn: fetcher(),
  });
};

const getKey = () => ['useGetJoinedChatRoomPreviews'];
const fetcher = () => async () => await ChatRoomAPI.getJoinedChatRoomPreviews();

useGetJoinedChatRoomPreviews.getkey = getKey;
useGetJoinedChatRoomPreviews.fetcher = fetcher;

export default useGetJoinedChatRoomPreviews;
