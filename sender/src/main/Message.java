package md.utm.pad;

// Aceasta este clasa care definește structura unui mesaj pentru Partea 1.
// Este "contractul" pe care toată echipa trebuie să-l respecte.
public class Message {
    private String type;    // Tipul mesajului (ex: "comanda", "notificare")
    private String payload; // Conținutul real al mesajului

    public Message(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Message{" + "type='" + type + '\'' + ", payload='" + payload + '\'' + '}';
    }
}