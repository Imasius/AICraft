package nimo.aic.proxy;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import nimo.aic.gui.GuiAIChest;
import nimo.aic.gui.GuiStorage;
import nimo.aic.tiles.TileEntityAIChest;
import nimo.aic.tiles.TileEntityStorage;
import nimo.aic.tiles.container.ContainerAIChest;
import nimo.aic.tiles.container.ContainerStorage;

public class GuiProxy implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityStorage) {
            return new ContainerStorage(player.inventory, (TileEntityStorage) te);
        } else if (te instanceof TileEntityAIChest) {
            return new ContainerAIChest(player.inventory, (TileEntityAIChest) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityStorage) {
            TileEntityStorage storageTE = (TileEntityStorage) te;
            return new GuiStorage(new ContainerStorage(player.inventory, storageTE));
        } else if (te instanceof TileEntityAIChest) {
            TileEntityAIChest aiChestTE = (TileEntityAIChest) te;
            return new GuiAIChest(new ContainerAIChest(player.inventory, aiChestTE));
        }
        return null;
    }
}
