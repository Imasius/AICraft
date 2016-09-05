package nimo.aic.network;


import io.grpc.stub.StreamObserver;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nimo.aic.AICraft;
import nimo.aic.grpc.AICraftService;
import nimo.aic.grpc.GetItemStackInfoResponse;

public class ResponseHandler {

    public static final ResponseHandler instance = new ResponseHandler();

    private ResponseHandler() {}

    public void getItemStackInfo(PacketRPC packet, MessageContext ctx) {
        StreamObserver<GetItemStackInfoResponse> responseObserver = AICraftService.responseMap.get(packet.getSequenceNr());
        if (responseObserver == null) {
            AICraft.log.warn("ResponseObserver is null.");
            return;
        }

        responseObserver.onNext((GetItemStackInfoResponse) packet.message);
        responseObserver.onCompleted();
    }
}
