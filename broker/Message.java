package md.utm.pad;

public class Message {
    private String type;
    private String payload;
    
    // Constructor pentru compatibilitate cu sender-ul
    public Message(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }
    
    // Constructor default (necesar pentru Gson)
    public Message() {}
    
    // Getters and setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    
    @Override
    public String toString() {
        return "Message{type='" + type + "', payload='" + payload + "'}";
    }
}