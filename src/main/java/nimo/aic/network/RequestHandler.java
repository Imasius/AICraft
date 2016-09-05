package nimo.aic.network;


import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.IItemHandler;
import nimo.aic.ai.AI;
import nimo.aic.grpc.*;
import nimo.aic.grpc.util.ConversionUtil;
import nimo.aic.tiles.AIStorage;
import nimo.aic.tiles.TileEntityController;
import nimo.aic.tiles.TileEntityId;

public class RequestHandler {

    public static final RequestHandler instance = new RequestHandler();

    private RequestHandler() {}

    public void setName(SetNameRequest request, MessageContext ctx) {
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            BlockPos pos = ConversionUtil.blockPosFrom(request.getPosition());

            TileEntityController controllerTE = (TileEntityController) world.getTileEntity(pos);
            controllerTE.setName(request.getName());
        });
    }

    public void setId(SetIdRequest request, MessageContext ctx) {
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            BlockPos pos = ConversionUtil.blockPosFrom(request.getPosition());
            TileEntityId idTE = (TileEntityId) world.getTileEntity(pos);
            idTE.setId(request.getId());
        });
    }

    public void transferItemStack(TransferItemStackRequest request, MessageContext ctx) {
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
            TileEntityId fromTE = AI.server().get(request.getFromId());
            TileEntityId toTE = AI.server().get(request.getToId());

            IItemHandler fromHandler = ((AIStorage) fromTE).getItemHandler();
            IItemHandler toHandler = ((AIStorage) toTE).getItemHandler();

            ItemStack fromStack = fromHandler.getStackInSlot(request.getFromSlot());
            fromStack = fromHandler.extractItem(request.getFromSlot(), fromStack.stackSize, false);
            toHandler.insertItem(request.getToSlot(), fromStack, false);
        });
    }

    public void getItemStackInfo(PacketRPC packet, MessageContext ctx) {
        GetItemStackInfoRequest request = (GetItemStackInfoRequest) packet.message;
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
            GetItemStackInfoResponse response = GetItemStackInfoResponse.newBuilder()
                    .setResult(Result.newBuilder().setCode(5)).build();
            PacketRPC responsePacket = new PacketRPC(response, packet.getSequenceNr());

            PacketHandler.INSTANCE.sendTo(responsePacket, ctx.getServerHandler().playerEntity);
        });
    }
}
