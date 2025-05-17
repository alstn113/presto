import useChatRoomParticipantInfo from '../../../hooks/chat/useChatRoomParticipantInfo';

interface TypingIndicatorProps {
  typingUserIds: Set<string>;
}

const TypingIndicator = ({ typingUserIds }: TypingIndicatorProps) => {
  const { getParticipantById } = useChatRoomParticipantInfo();
  const names = Array.from(typingUserIds)
    .map((id) => getParticipantById(id)?.username)
    .filter((name) => name !== undefined) as string[];

  const createMessage = () => {
    if (names.length === 0) return '';
    if (names.length === 1) return `${names[0]}님이 입력 중입니다...`;
    if (names.length === 2)
      return `${names[0]}님과 ${names[1]}님이 입력 중입니다...`;
    return `${names[0]}님 외 ${names.length - 1}명이 입력 중입니다...`;
  };

  return (
    <div className="mb-2 text-gray-500 text-sm min-h-[20px]">
      {createMessage()}
    </div>
  );
};

export default TypingIndicator;
