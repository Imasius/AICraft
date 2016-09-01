package nimo.aic.proxy;


import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import nimo.aic.AICraft;
import nimo.aic.ModBlocks;
import nimo.aic.compatibility.CompatibilityHandler;
import nimo.aic.network.PacketHandler;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        ModBlocks.init();

        PacketHandler.registerMessages(AICraft.MODID);
        CompatibilityHandler.registerWaila();
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(AICraft.instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
