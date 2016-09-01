package nimo.aic.network;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.IItemHandler;
import nimo.aic.AICraft;
import nimo.aic.grpc.TransferItemStackRequest;
import nimo.aic.tiles.AIStorage;
import nimo.aic.tiles.TileEntityId;


public class PacketTransferItemStack extends PacketRPC<TransferItemStackRequest> {

    public PacketTransferItemStack() {
        super(TransferItemStackRequest.class);
    }

    public PacketTransferItemStack(TransferItemStackRequest request) {
        super(request);
    }

    public static class Handler implements IMessageHandler<PacketTransferItemStack, IMessage> {
        @Override
        public IMessage onMessage(PacketTransferItemStack message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketTransferItemStack message, MessageContext ctx) {
            TransferItemStackRequest request = message.message;

            TileEntityId fromTE = AICraft.proxy.ai.get(request.getFromId());
            TileEntityId toTE = AICraft.proxy.ai.get(request.getToId());

            IItemHandler fromHandler = ((AIStorage) fromTE).getItemHandler();
            IItemHandler toHandler = ((AIStorage) toTE).getItemHandler();

            ItemStack fromStack = fromHandler.getStackInSlot(request.getFromSlot());
            fromStack = fromHandler.extractItem(request.getFromSlot(), fromStack.stackSize, false);
            toHandler.insertItem(request.getToSlot(), fromStack, false);
        }
    }
}
