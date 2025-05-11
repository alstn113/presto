interface Props {
  input: string;
  setInput: (value: string) => void;
  onSend: () => void;
}

const MessageInput = ({ input, setInput, onSend }: Props) => {
  return (
    <div className="flex gap-2">
      <input
        value={input}
        onChange={(e) => setInput(e.target.value)}
        onKeyUp={(e) => e.key === 'Enter' && onSend()}
        placeholder="메시지를 입력하세요"
        className="flex-1 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring focus:border-blue-400"
      />
      <button
        onClick={onSend}
        className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition"
      >
        보내기
      </button>
    </div>
  );
};

export default MessageInput;
