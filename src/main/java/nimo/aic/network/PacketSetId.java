package nimo.aic.network;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nimo.aic.grpc.SetIdRequest;
import nimo.aic.grpc.util.ConversionUtil;
import nimo.aic.tiles.TileEntityId;


public class PacketSetId extends PacketRPC<SetIdRequest> {

    public PacketSetId() {
        super(SetIdRequest.class);
    }

    public PacketSetId(SetIdRequest request) {
        super(request);
    }

    public static class Handler implements IMessageHandler<PacketSetId, IMessage> {
        @Override
        public IMessage onMessage(PacketSetId message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSetId message, MessageContext ctx) {
            SetIdRequest request = message.message;
            World world = ctx.getServerHandler().playerEntity.worldObj;
            BlockPos pos = ConversionUtil.blockPosFrom(request.getPosition());
            TileEntityId idTE = (TileEntityId) world.getTileEntity(pos);
            idTE.setId(request.getId());
        }
    }
}
