package nimo.aic.network;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nimo.aic.grpc.GetItemStackInfoRequest;
import nimo.aic.grpc.GetItemStackInfoResponse;
import nimo.aic.grpc.Result;


public class PacketGetItemStackInfo extends PacketRPC<GetItemStackInfoRequest> {

    public PacketGetItemStackInfo() {
        super(GetItemStackInfoRequest.class);
    }

    public PacketGetItemStackInfo(GetItemStackInfoRequest message) {
        super(message);
    }

    public static class Handler implements IMessageHandler<PacketGetItemStackInfo, IMessage> {
        @Override
        public IMessage onMessage(PacketGetItemStackInfo message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketGetItemStackInfo message, MessageContext ctx) {
            GetItemStackInfoResponse response = GetItemStackInfoResponse.newBuilder()
                    .setResult(Result.newBuilder().setCode(5)).build();
            PacketGetItemStackInfoResponse packet = new PacketGetItemStackInfoResponse(response);
            packet.setMessageId(message.messageId);

            PacketHandler.INSTANCE.sendTo(packet, ctx.getServerHandler().playerEntity);
        }
    }
}
