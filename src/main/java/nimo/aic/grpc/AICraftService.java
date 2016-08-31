package nimo.aic.grpc;


import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.IItemHandler;
import nimo.aic.AICraft;
import nimo.aic.ModBlocks;
import nimo.aic.ai.AI;
import nimo.aic.network.PacketHandler;
import nimo.aic.network.PacketSetId;
import nimo.aic.network.PacketSetName;
import nimo.aic.network.PacketTransferItemStack;
import nimo.aic.tiles.AIStorage;
import nimo.aic.tiles.TileEntityId;

public class AICraftService extends AICraftGrpc.AICraftImplBase {

    @Override
    public void setName(SetNameRequest request, StreamObserver<Empty> responseObserver) {
        // TODO: Log detailed request to trace
        AICraft.log.info("Received SetNameRequest.");
        if (Minecraft.getMinecraft().theWorld == null) {
            // TODO: Send "World not loaded" error
            AICraft.log.info("Unable to do anything with request as no world is loaded.");
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
            return;
        }

        BlockPos controllerPos = blockPosFrom(request.getPosition());
        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(controllerPos);
        Block block = blockState.getBlock();
        if (block == ModBlocks.controller) {
            PacketHandler.INSTANCE.sendToServer(new PacketSetName(request.getName(), controllerPos));
        } else {
            // TODO: Send "no controller at those coordinates" error
        }
        // TODO: Introduce sync variant that will send response as soon as client retrieves server update packet
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void setId(SetIdRequest request, StreamObserver<Empty> responseObserver) {
        // TODO: Log detailed request to trace
        AICraft.log.info("Received SetIdRequest");
        if (Minecraft.getMinecraft().theWorld == null) {
            // TODO: Send "World not loaded" error
            AICraft.log.info("Unable to do anything with request as no world is loaded.");
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
            return;
        }

        BlockPos blockPos = blockPosFrom(request.getPosition());
        TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(blockPos);
        if (te instanceof AIStorage) {
            PacketHandler.INSTANCE.sendToServer(new PacketSetId(request.getId(), blockPos));
        } else {
            // TODO: Send "no controller at those coordinates" error
        }
        // TODO: Introduce sync variant that will send response as soon as client retrieves server update packet
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void transferItemStack(TransferItemStackRequest request, StreamObserver<Empty> responseObserver) {
        // TODO: Log detailed request to trace
        AICraft.log.info("Received TransferItemStackRequest: {}", request);
        if (Minecraft.getMinecraft().theWorld == null) {
            // TODO: Send "World not loaded" error
            AICraft.log.info("Unable to do anything with request as no world is loaded.");
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
            return;
        }

        TileEntityId fromTE = AICraft.proxy.ai.get(request.getFromId());
        TileEntityId toTE = AICraft.proxy.ai.get(request.getToId());

        if (!(fromTE instanceof AIStorage) || !(toTE instanceof AIStorage)) {
            // TODO: Send "no storage blocks" error
            AICraft.log.info("One of the blocks is not able to store things");
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
            return;
        }

        IItemHandler fromHandler = ((AIStorage) fromTE).getItemHandler();
        IItemHandler toHandler = ((AIStorage) toTE).getItemHandler();

        ItemStack fromStack = fromHandler.getStackInSlot(request.getFromSlot());
        if (fromStack == null || fromStack.stackSize == 0) {
            // TODO: Send "empty" info
            AICraft.log.info("From slot is empty or does not have items");
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
            return;
        }

        ItemStack toStack = toHandler.getStackInSlot(request.getToSlot());
        if (toStack != null) {
            // TODO: Send "target not empty" error
            AICraft.log.info("To slot is not empty");
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
            return;
        }

        //Minecraft.getMinecraft().addScheduledTask(() -> {
        //    ItemStack fromSt = fromHandler.extractItem(request.getFromSlot(), fromStack.stackSize, false);
        //    toHandler.insertItem(request.getToSlot(), fromSt, false);
        //});

        PacketHandler.INSTANCE.sendToServer(new PacketTransferItemStack(request.getFromId(), request.getFromSlot(),
                request.getToId(), request.getToSlot()));

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    private BlockPos blockPosFrom(Position position) {
        return new BlockPos(position.getX(), position.getY(), position.getZ());
    }
}
