package nimo.aic.proxy;


import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nimo.aic.AICraft;
import nimo.aic.ModBlocks;
import nimo.aic.grpc.AICraftServer;

public class ClientProxy extends CommonProxy {

    private AICraftServer server;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        OBJLoader.INSTANCE.addDomain(AICraft.MODID);
        ModBlocks.initModels();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        ModBlocks.aiChest.initItemModel();
        server = new AICraftServer();
        server.start();
    }
}
