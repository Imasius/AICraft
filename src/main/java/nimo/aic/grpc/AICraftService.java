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
import nimo.aic.network.PacketSetName;

public class AICraftService extends AICraftGrpc.AICraftImplBase {

    @Override
    public void setName(SetNameRequest request, StreamObserver<Empty> responseObserver) {
        AICraft.log.info("Received SetNameRequest.");
        if (Minecraft.getMinecraft().theWorld == null) {
            // TODO: Send "World not loaded" error
            AICraft.log.info("Unable to do anything with request as no world is loaded.");
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
            return;
        }

        BlockPos controllerPos = new BlockPos(request.getPosition().getX(),
                request.getPosition().getY(), request.getPosition().getZ());
        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(controllerPos);
        Block block = blockState.getBlock();
        if (block == ModBlocks.controller) {
            PacketHandler.INSTANCE.sendToServer(new PacketSetName(request.getName(), controllerPos));
        } else {
            // TODO: Send "no controller at those coordinates" error
        }
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
