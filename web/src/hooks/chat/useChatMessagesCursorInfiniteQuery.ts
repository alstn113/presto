import { useInfiniteQuery, type InfiniteData } from '@tanstack/react-query';
import {
  ChatMessageApi,
  type ChatMessagesCursorRequest,
  type ChatMessagesCursorResponse,
} from '../../libs/api/chatMessageApi';
interface UseChatMessagesCursorInfiniteQueryProps {
  chatRoomId: string;
  initialData: ChatMessagesCursorResponse;
}
const useChatMessagesCursorInfiniteQuery = ({
  chatRoomId,
  initialData,
}: UseChatMessagesCursorInfiniteQueryProps) => {
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
    ChatMessagesCursorResponse,
    Error,
    InfiniteData<ChatMessagesCursorResponse>,
    string[],
    ChatMessagesCursorRequest
  >({
    queryKey: ['chatMessages', chatRoomId],
    queryFn: ({ pageParam }) => ChatMessageApi.getChatMessages(pageParam),
    getPreviousPageParam: (firstPage) => {
      const prevCursor = firstPage.prevCursor;
      // null | undefined인 경우 hasPreviousPage가 false로 설정됨
      if (!prevCursor) {
        return null;
      }

      return {
        direction: 'PREV',
        chatRoomId,
        cursorMessageId: prevCursor,
        lastMessageId: null,
      };
    },
    getNextPageParam: (lastPage) => {
      const nextCursor = lastPage.nextCursor;
      // null | undefined인 경우 hasNextPage가 false로 설정됨
      if (!nextCursor) {
        return null;
      }

      return {
        direction: 'NEXT',
        chatRoomId,
        cursorMessageId: nextCursor,
        lastMessageId: null,
      };
    },
    initialData: {
      pages: [initialData],
      // 초기 데이터의 pageParams는 임의의 값으로 설정
      pageParams: [
        {
          direction: 'NEXT',
          chatRoomId,
          cursorMessageId: 'id',
        },
      ],
    },
    // 초기 데이터의 pageParams는 임의의 값으로 설정
    initialPageParam: {
      direction: 'NEXT',
      chatRoomId,
      cursorMessageId: 'id',
    },
    enabled: false,
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

export default useChatMessagesCursorInfiniteQuery;
