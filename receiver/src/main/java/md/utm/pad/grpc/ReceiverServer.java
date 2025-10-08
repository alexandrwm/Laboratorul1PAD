package md.utm.pad.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class ReceiverServer {
    // Implementarea logicii serviciului
    static class BrokerServiceImpl extends BrokerServiceGrpc.BrokerServiceImplBase {
        @Override
        public void sendMessage(MessageRequest request, StreamObserver<MessageReply> responseObserver) {
            System.out.println("-> Received gRPC message: Type=" + request.getType() + ", Payload=" + request.getPayload());
            
            // AICI se poate adăuga logica de validare pentru payload-ul request-ului
            if (request.getType().isEmpty() || request.getPayload().isEmpty()) {
                 System.out.println("gRPC message is invalid (empty fields).");
            } else {
                 System.out.println("gRPC message is valid.");
            }
            
            // Trimitem un răspuns de confirmare
            MessageReply reply = MessageReply.newBuilder().setSuccess(true).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 7777; // Portul pe care ascultă serverul gRPC
        Server server = ServerBuilder.forPort(port)
                .addService(new BrokerServiceImpl())
                .build();
        
        server.start();
        System.out.println("gRPC Receiver server started, listening on " + port);
        server.awaitTermination();
    }
}