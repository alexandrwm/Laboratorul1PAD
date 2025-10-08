package md.utm.pad;

import java.net.ServerSocket;
import java.net.Socket;
import com.google.gson.Gson;

public class Receiver {
    public static void main(String[] args) throws Exception {
        int port = 7777; // Portul pe care ascultă acest Receiver
        System.out.println("Receiver (order processor) is listening on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) { // Ascultă pentru conexiuni încontinuu
                Socket brokerSocket = serverSocket.accept(); // Așteaptă conectarea Broker-ului
                
                // Pornește un fir nou de execuție pentru a gestiona mesajul
                new Thread(new MessageHandler(brokerSocket)).start();
            }
        }
    }
}

// Clasa care gestionează logica pentru un singur mesaj
class MessageHandler implements Runnable {
    private final Socket brokerSocket;
    private final Gson gson = new Gson();

    public MessageHandler(Socket socket) {
        this.brokerSocket = socket;
    }

    @Override
    public void run() {
        try (var in = new java.io.BufferedReader(new java.io.InputStreamReader(brokerSocket.getInputStream()))) {
            String receivedData = in.readLine();
            if (receivedData == null) return;

            System.out.println("-> Received raw data: " + receivedData);
            
            // Verificăm dacă mesajul este XML sau JSON
            if (receivedData.trim().startsWith("<")) {
                // Este XML, validăm cu XSD
                System.out.println("   Format detected: XML. Validating...");
                if (XmlValidator.validate(receivedData)) {
                    System.out.println("   XML is valid.");
                    // Aici ar urma parsarea XML și procesarea mesajului
                } else {
                    System.out.println("   XML is invalid.");
                }
            } else {
                // Presupunem că este JSON, deserializăm și validăm simplu
                System.out.println("   Format detected: JSON. Processing...");
                Message message = gson.fromJson(receivedData, Message.class);
                if (message != null && message.getType() != null && message.getPayload() != null) {
                    System.out.println("  JSON is valid and processed: " + message);
                } else {
                    System.out.println("  JSON is invalid or has missing fields.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
