package nimo.aic.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.IItemHandler;
import nimo.aic.AICraft;
import nimo.aic.grpc.Id;
import nimo.aic.tiles.AIStorage;
import nimo.aic.tiles.TileEntityId;


public class PacketTransferItemStack implements IMessage {

    private Id fromId;
    private int fromSlot;
    private Id toId;
    private int toSlot;

    public PacketTransferItemStack() {}

    public PacketTransferItemStack(Id fromId, int fromSlot, Id toId, int toSlot) {
        this.fromId = fromId;
        this.fromSlot = fromSlot;
        this.toId = toId;
        this.toSlot = toSlot;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        fromId = readId(buf);
        fromSlot = buf.readInt();
        toId = readId(buf);
        toSlot = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writeId(buf, fromId);
        buf.writeInt(fromSlot);
        writeId(buf, toId);
        buf.writeInt(toSlot);
    }

    private void writeId(ByteBuf buf, Id id) {
        ByteBufUtils.writeUTF8String(buf, id.getGroup());
        ByteBufUtils.writeUTF8String(buf, id.getId());
    }

    private Id readId(ByteBuf buf) {
        String group = ByteBufUtils.readUTF8String(buf);
        String id = ByteBufUtils.readUTF8String(buf);
        return Id.newBuilder().setGroup(group).setId(id).build();
    }

    public static class Handler implements IMessageHandler<PacketTransferItemStack, IMessage> {
        @Override
        public IMessage onMessage(PacketTransferItemStack message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketTransferItemStack message, MessageContext ctx) {
            TileEntityId fromTE = AICraft.proxy.ai.get(message.fromId);
            TileEntityId toTE = AICraft.proxy.ai.get(message.toId);

            IItemHandler fromHandler = ((AIStorage) fromTE).getItemHandler();
            IItemHandler toHandler = ((AIStorage) toTE).getItemHandler();

            ItemStack fromStack = fromHandler.getStackInSlot(message.fromSlot);
            fromStack = fromHandler.extractItem(message.fromSlot, fromStack.stackSize, false);
            toHandler.insertItem(message.toSlot, fromStack, false);
        }
    }
}
