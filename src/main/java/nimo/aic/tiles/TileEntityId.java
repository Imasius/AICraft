package nimo.aic.tiles;


import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import nimo.aic.grpc.Id;


/**
 * Base class for all TEs that should be "nameable" using SetId gRPC call
 */
public abstract class TileEntityId extends TileEntity {

    protected Id id;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.id = Id.newBuilder().setGroup(compound.getString("group")).setId(compound.getString("id")).build();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (id != null) {
            compound.setString("group", id.getGroup());
            compound.setString("id", id.getId());
        }
        return compound;
    }

    public void setId(Id id) {
        this.id = id;
        markDirty();
        IBlockState state = worldObj.getBlockState(getPos());
        worldObj.notifyBlockUpdate(getPos(), state, state, 3);
    }
}
