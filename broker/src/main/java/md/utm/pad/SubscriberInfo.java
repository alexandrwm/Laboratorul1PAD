package md.utm.pad;

public class SubscriberInfo {
    private String subscriberId;
    private String host;
    private int port;
    
    public SubscriberInfo(String subscriberId, String host, int port) {
        this.subscriberId = subscriberId;
        this.host = host;
        this.port = port;
    }
    
    // Getters
    public String getSubscriberId() { return subscriberId; }
    public String getHost() { return host; }
    public int getPort() { return port; }
    
    @Override
    public String toString() {
        return subscriberId + "@" + host + ":" + port;
    }
}