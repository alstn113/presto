import { create } from 'zustand';
import { immer } from 'zustand/middleware/immer';
import type { ChatRoom } from '../libs/types';

type States = {
  selectedChatRoom: ChatRoom | null;
};

type Actions = {
  selectChatRoom: (chatRoom: ChatRoom) => void;
};

const useChatRoomStore = create<States & Actions>()(
  immer((set) => ({
    selectedChatRoom: null,
    selectChatRoom: (chatRoom: ChatRoom) => {
      set((state) => {
        state.selectedChatRoom = chatRoom;
      });
    },
  }))
);

export default useChatRoomStore;
