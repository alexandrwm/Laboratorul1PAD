package md.utm.pad;

import java.io.*;
import java.net.*;
import java.util.*;

public class JsonClientHandler implements Runnable {
    private Socket clientSocket;
    private MessageBroker broker;
    
    public JsonClientHandler(Socket socket, MessageBroker broker) {
        this.clientSocket = socket;
        this.broker = broker;
    }
    
    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String jsonMessage = in.readLine();
            if (jsonMessage != null) {
                System.out.println("Received: " + jsonMessage);
                processJsonMessage(jsonMessage, out);
            }
        } catch (IOException e) {
            System.err.println("Handler error: " + e.getMessage());
        }
    }
    
    private void processJsonMessage(String jsonMessage, PrintWriter out) {
        try {
            // Parsare JSON simplă fără Gson
            String type = extractValue(jsonMessage, "type");
            String payload = extractValue(jsonMessage, "payload");
            
            Message message = new Message(type, payload);
            System.out.println("Message processed - Type: " + type + ", Payload: " + payload);
            
            broker.publishToSubject(message.getType(), message);
            
            // Răspuns simplu fără Gson
            String response = "{\"status\":\"success\",\"message\":\"Received by broker\"}";
            out.println(response);
            
        } catch (Exception e) {
            System.err.println("JSON processing error: " + e.getMessage());
            String errorResponse = "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
            out.println(errorResponse);
        }
    }
    
    private String extractValue(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start == -1) return "";
        start += search.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}