package md.utm.pad;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class MessageBroker {
    private ServerSocket serverSocket;
    private int port = 6666;
    private Map<String, List<SubscriberInfo>> subscribers;
    private ExecutorService threadPool;
    private boolean isRunning;
    
    public MessageBroker(int port) {
        this.port = port;
        this.subscribers = new ConcurrentHashMap<>();
        this.threadPool = Executors.newFixedThreadPool(10);
        this.isRunning = false;
    }
    
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            isRunning = true;
            
            System.out.println("Broker started on port " + port);
            System.out.println("Waiting for messages...");
            
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("🔗 New connection from: " + clientSocket.getInetAddress());
                threadPool.execute(new JsonClientHandler(clientSocket, this));
            }
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public void subscribe(String subject, SubscriberInfo subscriber) {
        subscribers.computeIfAbsent(subject, k -> new CopyOnWriteArrayList<>())
                  .add(subscriber);
        System.out.println(subscriber.getSubscriberId() + " subscribed to '" + subject + "'");
    }
    
    public void publishToSubject(String subject, Message message) {
        List<SubscriberInfo> subjectSubscribers = subscribers.get(subject);
        if (subjectSubscribers != null && !subjectSubscribers.isEmpty()) {
            System.out.println("Routing to " + subjectSubscribers.size() + " subscriber(s)");
            for (SubscriberInfo subscriber : subjectSubscribers) {
                forwardToSubscriber(subscriber, message);
            }
        } else {
            System.out.println("No subscribers for '" + subject + "'");
        }
    }
    
    private void forwardToSubscriber(SubscriberInfo subscriber, Message message) {
        try (Socket subscriberSocket = new Socket(subscriber.getHost(), subscriber.getPort());
             PrintWriter out = new PrintWriter(subscriberSocket.getOutputStream(), true)) {
            
            // Serializare JSON simplă fără Gson
            String messageJson = "{\"type\":\"" + message.getType() + "\",\"payload\":\"" + message.getPayload() + "\"}";
            out.println(messageJson);
            System.out.println("Sent to " + subscriber.getSubscriberId());
            
        } catch (IOException e) {
            System.err.println("Error sending to " + subscriber.getSubscriberId());
        }
    }
}