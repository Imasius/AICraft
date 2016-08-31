package nimo.aic.network;


import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    public static int nextId() {
        return packetId++;
    }

    public static void registerMessages(String channelName) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        registerMessages();
    }

    public static void registerMessages() {
        INSTANCE.registerMessage(PacketSetName.Handler.class, PacketSetName.class, nextId(), Side.SERVER);
        INSTANCE.registerMessage(PacketSetId.Handler.class, PacketSetId.class, nextId(), Side.SERVER);
        INSTANCE.registerMessage(PacketTransferItemStack.Handler.class, PacketTransferItemStack.class, nextId(), Side.SERVER);
    }
}
