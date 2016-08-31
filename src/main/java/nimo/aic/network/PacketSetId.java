package nimo.aic.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nimo.aic.grpc.Id;
import nimo.aic.tiles.TileController;
import nimo.aic.tiles.TileStorage;


public class PacketSetId implements IMessage {

    private Id id;
    private BlockPos pos;

    public PacketSetId() {}

    public PacketSetId(Id id, BlockPos pos) {
        this.id = id;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String group = ByteBufUtils.readUTF8String(buf);
        String id = ByteBufUtils.readUTF8String(buf);
        this.id = Id.newBuilder().setGroup(group).setId(id).build();

        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, id.getGroup());
        ByteBufUtils.writeUTF8String(buf, id.getId());
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    public static class Handler implements IMessageHandler<PacketSetId, IMessage> {
        @Override
        public IMessage onMessage(PacketSetId message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSetId message, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            // TODO: needs a generic version via interface or base class
            TileStorage storageTE = (TileStorage) world.getTileEntity(message.pos);
            storageTE.setId(message.id);
        }
    }
}
