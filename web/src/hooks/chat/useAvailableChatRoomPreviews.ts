import { useSuspenseQuery } from '@tanstack/react-query';
import { ChatRoomAPI } from '../../libs/api/chatRoomAPI';

const useGetAvailableChatRoomPreviews = () => {
  return useSuspenseQuery({
    queryKey: getKey(),
    queryFn: fetcher(),
  });
};

const getKey = () => ['useGetAvailableChatRooms'];
const fetcher = () => async () =>
  await ChatRoomAPI.getAvailableChatRoomPreviews();

useGetAvailableChatRoomPreviews.getkey = getKey;
useGetAvailableChatRoomPreviews.fetcher = fetcher;

export default useGetAvailableChatRoomPreviews;
