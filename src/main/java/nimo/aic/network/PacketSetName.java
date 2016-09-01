package nimo.aic.network;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nimo.aic.grpc.SetNameRequest;
import nimo.aic.grpc.util.ConversionUtil;
import nimo.aic.tiles.TileEntityController;


public class PacketSetName extends PacketRPC<SetNameRequest> {

    public PacketSetName() {
        super(SetNameRequest.class);
    }

    public PacketSetName(SetNameRequest request) {
        super(request);
    }

    public static class Handler implements IMessageHandler<PacketSetName, IMessage> {
        @Override
        public IMessage onMessage(PacketSetName message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSetName message, MessageContext ctx) {
            SetNameRequest request = message.message;
            World world = ctx.getServerHandler().playerEntity.worldObj;
            BlockPos pos = ConversionUtil.blockPosFrom(request.getPosition());

            TileEntityController controllerTE = (TileEntityController) world.getTileEntity(pos);
            controllerTE.setName(request.getName());
        }
    }
}
