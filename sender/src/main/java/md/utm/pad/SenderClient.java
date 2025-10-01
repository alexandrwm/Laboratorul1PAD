package md.utm.pad.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SenderClient {
    public static void main(String[] args) {
        // Adresa serverului gRPC al Broker-ului
        String brokerHost = "localhost";
        int brokerPort = 6666;

        // 1. Creează un canal de comunicare securizat către Broker
        ManagedChannel channel = ManagedChannelBuilder.forAddress(brokerHost, brokerPort)
                .usePlaintext() // Folosim conexiune necriptată pentru testare
                .build();

        // 2. Creează un "stub" (un client) care știe cum să apeleze funcțiile de pe server
        BrokerServiceGrpc.BrokerServiceBlockingStub stub = BrokerServiceGrpc.newBlockingStub(channel);

        // 3. Construiește obiectul de cerere folosind clasele generate automat
        MessageRequest request = MessageRequest.newBuilder()
                .setType("gRPC-order")
                .setPayload("{'product':'Monitor Dell','quantity':2}")
                .build();
        
        System.out.println("-> Sending gRPC message to Broker...");

        // 4. Apelează funcția de la distanță ca pe o metodă locală normală
        try {
            MessageReply reply = stub.sendMessage(request);
            System.out.println("✅ Reply from Broker: Success = " + reply.getSuccess());
        } catch (Exception e) {
            System.err.println("Error calling Broker: " + e.getMessage());
        }

        // 5. Închide canalul de comunicare
        channel.shutdown();
    }
}