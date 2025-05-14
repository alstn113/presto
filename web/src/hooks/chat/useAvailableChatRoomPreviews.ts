import { useSuspenseQuery } from '@tanstack/react-query';
import { ChatRoomApi } from '../../libs/api/chatRoomApi.ts';

const useGetAvailableChatRoomPreviews = () => {
  return useSuspenseQuery({
    queryKey: getKey(),
    queryFn: fetcher(),
  });
};

const getKey = () => ['useGetAvailableChatRooms'];
const fetcher = () => async () =>
  await ChatRoomApi.getAvailableChatRoomPreviews();

useGetAvailableChatRoomPreviews.getkey = getKey;
useGetAvailableChatRoomPreviews.fetcher = fetcher;

export default useGetAvailableChatRoomPreviews;
