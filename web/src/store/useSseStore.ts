import { create } from 'zustand';
import { immer } from 'zustand/middleware/immer';
import type { JoinedChatRoomPreviewUpdatedEvent } from '../hooks/socket/types';

type States = {
  joinedChatRoomPreviewUpdated: JoinedChatRoomPreviewUpdatedEvent | null;
};

type Actions = {
  setJoinedChatRoomPreviewUpdated: (
    event: JoinedChatRoomPreviewUpdatedEvent
  ) => void;
};

const initialState: States = {
  joinedChatRoomPreviewUpdated: null,
};

const useSseStore = create<States & Actions>()(
  immer((set) => ({
    ...initialState,

    setJoinedChatRoomPreviewUpdated: (event) =>
      set((state) => {
        state.joinedChatRoomPreviewUpdated = event;
      }),
  }))
);

export default useSseStore;
