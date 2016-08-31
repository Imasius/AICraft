package nimo.aic.blocks;


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
import nimo.aic.tiles.TileAIChest;
import nimo.aic.tiles.TileStorage;

import javax.annotation.Nullable;

public class BlockAIChest extends Block implements ITileEntityProvider {

    private static final int GUI_ID = 2;

    public BlockAIChest() {
        super(Material.IRON);
        setUnlocalizedName(AICraft.MODID + ".aichest");
        setRegistryName("aichest");

        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
        GameRegistry.registerTileEntity(TileAIChest.class, AICraft.MODID + "_aichest");

        setCreativeTab(AICraft.CREATIVE_TAB);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileAIChest();
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity aiChestTE = worldIn.getTileEntity(pos);
        if (!(aiChestTE instanceof TileAIChest)) {
            return false;
        }

        if (worldIn.isRemote) {
            return true;
        }

        playerIn.openGui(AICraft.instance, GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}
