import { Client, type StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { SOCKET_URL } from '../../hooks/socket/socketPaths';

interface Subscriptions {
  [topic: string]: {
    subscription: StompSubscription;
    callback: (message: unknown) => void;
  };
}

interface SubscriptionQueue {
  [topic: string]: (message: unknown) => void;
}

interface SocketConfig {
  url: string;
  reconnectDelay?: number;
  debug?: boolean;
}

class SocketClient {
  private static instance: SocketClient;

  private client: Client | null = null;
  private subscriptions: Subscriptions = {};
  private subscriptionQueue: SubscriptionQueue = {};
  private config: SocketConfig;

  public static getInstance(): SocketClient {
    if (!SocketClient.instance) {
      SocketClient.instance = new SocketClient({
        url: SOCKET_URL,
      });
    }

    return SocketClient.instance;
  }

  private constructor(config: SocketConfig) {
    this.config = {
      reconnectDelay: 5000,
      debug: false,
      ...config,
    };
  }

  public connect() {
    // active는 연결 중이거나 연결이 완료된 경우 - 중복 연결 방지
    if (this.client?.active) return;

    this.client = new Client({
      webSocketFactory: () => new SockJS(this.config.url),
      reconnectDelay: this.config.reconnectDelay,
      debug: (msg) => {
        if (this.config.debug) {
          console.log('[STOMP] Debug:', msg);
        }
      },
      onConnect: () => {
        console.log('✅ STOMP Connected');
        this.resubscribeAll(); // 재연결 시 이전 구독 목록 불러오기
        this.flushSubscriptionQueue(); // 연결이 되지 않은 상태에서 구독 요청이 들어온 경우 연결 시 구독 처리
      },
      onStompError: (frame) => console.error('❌ STOMP Error:', frame),
    });

    this.client.activate();
  }

  public subscribe<T>(topic: string, callback: (message: T) => void) {
    if (this.subscriptions[topic]) {
      console.warn(`⚠️ Already subscribed to topic: ${topic}`);
      return;
    }

    if (!this.client?.connected) {
      console.log(`📝 Queuing subscription for topic: ${topic}`);
      this.subscriptionQueue[topic] = callback as (message: unknown) => void;
      return;
    }

    const stompSub = this.client.subscribe(topic, (message) => {
      if (!message.body) return;
      const parsedMessage = JSON.parse(message.body);
      callback(parsedMessage);
    });

    this.subscriptions[topic] = {
      subscription: stompSub,
      callback: callback as (message: unknown) => void,
    };

    console.log(`📥 Subscribed to topic: ${topic}`);
  }

  public unsubscribe(topic: string) {
    if (!this.client?.connected || !this.subscriptions[topic]) return;

    this.subscriptions[topic].subscription.unsubscribe();
    delete this.subscriptions[topic];

    console.log(`📤 Unsubscribed from topic: ${topic}`);
  }

  public publish<T>(destination: string, body: T) {
    if (!this.client?.connected) {
      console.error('❌ Cannot publish, client is not connected');
      return;
    }

    this.client.publish({ destination, body: JSON.stringify(body) });
    console.log(`📤 Published to ${destination}:`, body);
  }

  public disconnect() {
    if (!this.client) return;

    Object.keys(this.subscriptions).forEach((topic) => this.unsubscribe(topic));

    this.client.deactivate();
    this.client = null;
    this.subscriptions = {};
    this.subscriptionQueue = {};

    console.log('🔴 STOMP Disconnected');
  }

  private flushSubscriptionQueue() {
    Object.keys(this.subscriptionQueue).forEach((topic) => {
      const callback = this.subscriptionQueue[topic];
      this.subscribe(topic, callback);
    });

    this.subscriptionQueue = {};
  }

  private resubscribeAll() {
    Object.entries(this.subscriptions).forEach(([topic, { callback }]) => {
      if (!this.client) return;

      const stompSub = this.client.subscribe(topic, (message) => {
        if (!message.body) return;
        const parsedMessage = JSON.parse(message.body);
        callback(parsedMessage);
      });

      this.subscriptions[topic] = {
        subscription: stompSub,
        callback,
      };

      console.log(`♻️ Resubscribed to topic: ${topic}`);
    });
  }
}

export const socketClient = SocketClient.getInstance();
