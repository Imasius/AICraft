package nimo.aic.blocks;


import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nimo.aic.AICraft;
import nimo.aic.compatibility.IWailaInfoProvider;
import nimo.aic.tiles.TileEntityController;
import nimo.aic.tiles.TileEntityId;
import nimo.aic.tiles.TileEntityStorage;

import javax.annotation.Nullable;
import java.util.List;

public class BlockStorage extends Block implements ITileEntityProvider, IWailaInfoProvider {

    private static final int GUI_ID = 1;

    public BlockStorage() {
        super(Material.IRON);
        setUnlocalizedName(AICraft.MODID + ".storage");
        setRegistryName("storage");

        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
        GameRegistry.registerTileEntity(TileEntityStorage.class, AICraft.MODID + "_storage");

        setCreativeTab(AICraft.CREATIVE_TAB);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityStorage();
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity storageTE = worldIn.getTileEntity(pos);
        if (!(storageTE instanceof TileEntityStorage)) {
            return false;
        }

        if (worldIn.isRemote) {
            return true;
        }

        playerIn.openGui(AICraft.instance, GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        // TODO: Same as AIChest, also making string out of Id is hard coded
        TileEntity te = accessor.getTileEntity();
        if (te instanceof TileEntityId) {
            TileEntityId idTE = (TileEntityId) te;
            if (idTE.getId() != null) {
                currentTip.add("Id: " + idTE.getId().getGroup() + ":" + idTE.getId().getId());
            }
        }
        return currentTip;
    }
}
