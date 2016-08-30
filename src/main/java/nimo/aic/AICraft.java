package nimo.aic;


import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nimo.aic.proxy.CommonProxy;
import org.apache.logging.log4j.Logger;

@Mod(modid = AICraft.MODID, name = AICraft.MODNAME, version = AICraft.MODVERSION)
public class AICraft {

    public static final String MODID = "aicraft";
    public static final String MODNAME = "AICraft";
    public static final String MODVERSION = "1.0.0";

    public static final CreativeTab CREATIVE_TAB = new CreativeTab();

    @SidedProxy(clientSide = "nimo.aic.proxy.ClientProxy", serverSide = "nimo.aic.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static AICraft instance;

    public static Logger log;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
