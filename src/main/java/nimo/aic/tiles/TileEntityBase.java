package nimo.aic.tiles;


import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Thanks to Crazypants and HenryLoenwind
 */
public abstract class TileEntityBase extends TileEntity {

    @Override
    public void readFromNBT(NBTTagCompound root) {
        super.readFromNBT(root);
        readCustomNBT(root);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound root) {
        super.writeToNBT(root);
        writeCustomNBT(root);
        return root;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(getPos(), 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    public boolean canPlayerAccess(EntityPlayer player) {
        return !isInvalid() && player.getDistanceSqToCenter(getPos().add(0.5, 0.5, 0.5)) <= 64D;
    }

    protected void updateBlock() {
        updateBlock(3);
    }

    protected void updateBlock(int flag) {
        if (worldObj != null) {
            IBlockState bs = worldObj.getBlockState(getPos());
            worldObj.notifyBlockUpdate(pos, bs, bs, flag);
        }
    }

    protected abstract void readCustomNBT(NBTTagCompound root);
    protected abstract void writeCustomNBT(NBTTagCompound root);
}
