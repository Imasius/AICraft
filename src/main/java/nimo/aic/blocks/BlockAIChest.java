package nimo.aic.blocks;


import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nimo.aic.AICraft;
import nimo.aic.tiles.TileAIChest;
import nimo.aic.tiles.TileController;
import nimo.aic.tiles.tesr.TesrAIChest;

import javax.annotation.Nullable;

public class BlockAIChest extends BlockContainer {

    public BlockAIChest() {
        super(Material.IRON);
        setUnlocalizedName(AICraft.MODID + ".aichest");
        setRegistryName("aichest");
        setCreativeTab(AICraft.CREATIVE_TAB);

        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
        GameRegistry.registerTileEntity(TileController.class, AICraft.MODID + "_aichest");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {

        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
        //        new ModelResourceLocation(getRegistryName(), "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileAIChest.class, new TesrAIChest());
    }

    @SideOnly(Side.CLIENT)
    public void initItemModel() {
        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        mesher.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity chestTE = worldIn.getTileEntity(pos);
        if (chestTE == null || !(chestTE instanceof TileAIChest)) {
            return true;
        }

        if (worldIn.isSideSolid(pos.add(0, 1, 0), EnumFacing.DOWN)) {
            return true;
        }

        if (worldIn.isRemote) {
            return true;
        }

        playerIn.openGui(AICraft.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileAIChest();
    }


}
