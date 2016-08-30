package nimo.aic.proxy;


import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nimo.aic.AICraft;
import nimo.aic.ModBlocks;
import nimo.aic.network.PacketHandler;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        ModBlocks.init();

        PacketHandler.registerMessages(AICraft.MODID);
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
