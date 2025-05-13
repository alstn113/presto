import { useCallback, useEffect } from 'react';
import { socketClient } from '../../libs/socket/socketClient';

const useSocket = <TSend, TReceive>(
  sendPath: string,
  subscribePath: string,
  onReceived: (event: TReceive) => void,
  onSend?: (event: TSend) => void
) => {
  useEffect(() => {
    socketClient.subscribe<TReceive>(subscribePath, onReceived);

    return () => {
      socketClient.unsubscribe(subscribePath);
    };
  }, [subscribePath]);

  const send = useCallback(
    (event: TSend) => {
      socketClient.publish(sendPath, event);
      onSend?.(event);
    },
    [sendPath]
  );

  return { send };
};

export default useSocket;
