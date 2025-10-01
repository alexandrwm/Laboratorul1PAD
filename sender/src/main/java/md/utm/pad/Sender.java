package md.utm.pad;

import com.google.gson.Gson;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender {
    public static void main(String[] args) {
        try {
            // Adresa unde ascultă Broker-ul
            String brokerHost = "localhost";
            int brokerPort = 6666;

            // 1. Creează un obiect Message (conform "contractului")
            Message message = new Message("order", "{'product':'Laptop MSI','quantity':1}");

            // 2. Inițializează Gson pentru a converti obiectul în JSON
            Gson gson = new Gson();
            String jsonMessage = gson.toJson(message);

            // 3. Conectează-te la Broker și trimite mesajul
            try (Socket socket = new Socket(brokerHost, brokerPort);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                
                System.out.println("-> Sending message to Broker: " + jsonMessage);
                out.println(jsonMessage); // Trimite textul JSON
            }

            System.out.println("Message sent successfully.");

        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}