package nimo.aic;


import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nimo.aic.blocks.BlockAIChest;
import nimo.aic.blocks.BlockController;

public class ModBlocks {

    public static BlockController controller;
    public static BlockAIChest aiChest;

    public static void init() {
        controller = new BlockController();
        aiChest = new BlockAIChest();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        controller.initModel();
        aiChest.initModel();
    }
}
