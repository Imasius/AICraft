package nimo.aic;


import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nimo.aic.blocks.BlockController;
import nimo.aic.blocks.BlockStorage;

public class ModBlocks {

    public static BlockController controller;
    public static BlockStorage storage;

    public static void init() {
        controller = new BlockController();
        storage = new BlockStorage();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        controller.initModel();
        storage.initModel();
    }
}
