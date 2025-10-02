package md.utm.pad;

public class BrokerMain {
    public static void main(String[] args) {
        System.out.println("=== SIMPLE BROKER STARTING ===");
        
        try {
            MessageBroker broker = new MessageBroker(6666);
            broker.start();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}