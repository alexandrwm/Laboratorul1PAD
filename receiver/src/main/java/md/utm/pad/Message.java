package md.utm.pad;

public class Message {
    private String type;
    private String payload;

    public Message(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getType() { return type; }
    public String getPayload() { return payload; }

    @Override
    public String toString() {
        return "Message{" + "type='" + type + '\'' + ", payload='" + payload + '\'' + '}';
    }
}