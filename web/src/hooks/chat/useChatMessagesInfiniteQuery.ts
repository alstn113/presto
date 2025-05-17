import { useInfiniteQuery, type InfiniteData } from '@tanstack/react-query';
import {
  ChatMessageApi,
  type ChatMessageDto,
  type ChatMessagesRequest,
} from '../../libs/api/chatMessageApi';
import type { CursorResult } from '../../libs/api/response/apiResponse';
interface UseChatMessagesInfiniteQueryProps {
  chatRoomId: string;
  lastMessageId: string | null;
}
const useChatMessagesInfiniteQuery = ({
  chatRoomId,
  lastMessageId,
}: UseChatMessagesInfiniteQueryProps) => {
  const {
    data,
    fetchNextPage,
    fetchPreviousPage,
    hasNextPage,
    hasPreviousPage,
    isFetchingNextPage,
    isFetchingPreviousPage,
    isLoading,
    isError,
    error,
  } = useInfiniteQuery<
    CursorResult<ChatMessageDto>,
    Error,
    InfiniteData<CursorResult<ChatMessageDto>>,
    string[],
    ChatMessagesRequest
  >({
    queryKey: ['chatMessages', chatRoomId],
    queryFn: ({ pageParam }) => ChatMessageApi.getChatMessages(pageParam),
    initialPageParam: {
      direction: 'INIT',
      chatRoomId,
      cursorMessageId: null,
      lastMessageId,
    },
    getNextPageParam: (lastPage) => {
      const nextCursor = lastPage.nextCursor;
      if (!nextCursor) return undefined; // 실행하지 않음.
      return {
        direction: 'NEXT',
        chatRoomId,
        cursorMessageId: nextCursor,
        lastMessageId: null,
      };
    },
    getPreviousPageParam: (firstPage) => {
      const prevCursor = firstPage.prevCursor;
      if (!prevCursor) return undefined; // 실행하지 않음.
      return {
        direction: 'PREV',
        chatRoomId,
        cursorMessageId: prevCursor,
        lastMessageId: null,
      };
    },
    enabled: !!chatRoomId, // chatRoomId가 있을 때만 쿼리 실행
  });

  return {
    data,
    fetchNextPage,
    fetchPreviousPage,
    hasNextPage,
    hasPreviousPage,
    isFetchingNextPage,
    isFetchingPreviousPage,
    isLoading,
    isError,
    error,
  };
};

export default useChatMessagesInfiniteQuery;
