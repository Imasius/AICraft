package nimo.aic.compatibility;


import net.minecraftforge.fml.common.Loader;

public class CompatibilityHandler {

    public static void registerWaila() {
        if (Loader.isModLoaded("Waila")) {
            WailaCompatibility.register();
        }
    }
}
