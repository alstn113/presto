import { Client, type StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BASE_URL } from '../../constants';

interface Subscriptions {
  [topic: string]: StompSubscription;
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
        url: `${BASE_URL}/ws`,
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
        this.flushSubscriptionQueue();
      },
      onStompError: (frame) => console.error('❌ STOMP Error:', frame),
    });

    this.client.activate();
  }

  public subscribe<T>(topic: string, callback: (message: T) => void) {
    if (!this.client?.connected) {
      if (!this.subscriptionQueue[topic]) {
        this.subscriptionQueue[topic] = callback as (message: unknown) => void;
        console.log(`📝 Subscription queued for topic: ${topic}`);
        return;
      }
    }

    if (!this.client) return;
    if (this.subscriptions[topic]) return;
    this.subscriptions[topic] = this.client.subscribe(topic, (message) => {
      if (!message.body) return;
      const parsedMessage = JSON.parse(message.body);
      callback(parsedMessage);
    });

    console.log(`📥 Subscribed to topic: ${topic}`);
  }

  public unsubscribe(topic: string) {
    if (!this.client?.connected || !this.subscriptions[topic]) return;

    this.subscriptions[topic].unsubscribe();
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
}

export const socketClient = SocketClient.getInstance();
