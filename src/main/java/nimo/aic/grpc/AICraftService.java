package nimo.aic.grpc;


import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import nimo.aic.AICraft;
import nimo.aic.ModBlocks;
import nimo.aic.network.PacketHandler;
import nimo.aic.network.PacketSetId;
import nimo.aic.network.PacketSetName;

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
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock();
        if (block == ModBlocks.storage) {
            PacketHandler.INSTANCE.sendToServer(new PacketSetId(request.getId(), blockPos));
        } else {
            // TODO: Send "no controller at those coordinates" error
        }
        // TODO: Introduce sync variant that will send response as soon as client retrieves server update packet
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    private BlockPos blockPosFrom(Position position) {
        return new BlockPos(position.getX(), position.getY(), position.getZ());
    }
}
