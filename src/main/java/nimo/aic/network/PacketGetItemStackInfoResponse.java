package nimo.aic.network;

import io.grpc.stub.StreamObserver;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nimo.aic.AICraft;
import nimo.aic.grpc.AICraftService;
import nimo.aic.grpc.GetItemStackInfoResponse;


public class PacketGetItemStackInfoResponse extends PacketRPC<GetItemStackInfoResponse> {

    public PacketGetItemStackInfoResponse() {
        super(GetItemStackInfoResponse.class);
    }

    public PacketGetItemStackInfoResponse(GetItemStackInfoResponse message) {
        super(message);
    }

    public static class Handler implements IMessageHandler<PacketGetItemStackInfoResponse, IMessage> {
        @Override
        public IMessage onMessage(PacketGetItemStackInfoResponse message, MessageContext ctx) {
            StreamObserver<GetItemStackInfoResponse> responseObserver = AICraftService.responseMap.get(message.getMessageId());
            if (responseObserver == null) {
                AICraft.log.warn("ResponseObserver is null.");
                return null;
            }

            responseObserver.onNext(message.message);
            responseObserver.onCompleted();

            return null;
        }
    }
}
