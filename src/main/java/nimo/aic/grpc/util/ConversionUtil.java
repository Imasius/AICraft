package nimo.aic.grpc.util;


import net.minecraft.util.math.BlockPos;
import nimo.aic.grpc.Position;

public class ConversionUtil {

    public static BlockPos blockPosFrom(Position position) {
        return new BlockPos(position.getX(), position.getY(), position.getZ());
    }
}
