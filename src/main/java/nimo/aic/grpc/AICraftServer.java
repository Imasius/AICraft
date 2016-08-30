package nimo.aic.grpc;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import nimo.aic.AICraft;

import java.io.IOException;

public class AICraftServer {

    private static final int PORT = 40200;

    private final Server server;

    public AICraftServer() {
        server = ServerBuilder.forPort(PORT).addService(new AICraftService()).build();
    }

    public void start() {
        try {
            server.start();
            AICraft.log.info("gRPC server started, listening on port {}", PORT);
        } catch (IOException e) {
            AICraft.log.error("Unable to start gRPC server", e);
        }
    }
}
