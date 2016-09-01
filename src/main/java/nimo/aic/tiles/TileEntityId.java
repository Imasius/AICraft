package nimo.aic.tiles;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import nimo.aic.ai.AI;
import nimo.aic.grpc.Id;


/**
 * Base class for all TEs that should be "nameable" using SetId gRPC call
 */
public abstract class TileEntityId extends TileEntityBase {

    protected Id aiId;
    private boolean initialized = false;

    @Override
    protected void readCustomNBT(NBTTagCompound root) {
        this.aiId = Id.newBuilder().setGroup(root.getString("group")).setId(root.getString("aiid")).build();
        init(worldObj);
    }

    @Override
    public void onLoad() {
        init(worldObj);
    }

    private void init(World world) {
        if (!initialized && worldObj != null && aiId != null) {
            AI.forWorld(world).register(aiId, this);
            initialized = true;
        }
    }

    @Override
    protected void writeCustomNBT(NBTTagCompound root) {
        if (aiId != null) {
            root.setString("group", aiId.getGroup());
            root.setString("aiid", aiId.getId());
        }
    }

    public void setId(Id id) {
        if (this.aiId != null) {
            AI.forWorld(worldObj).unregister(aiId);
        }
        this.aiId = id;
        AI.forWorld(worldObj).register(id, this);
        markDirty();
        updateBlock(7);
    }

    public Id getId() {
        return aiId;
    }
}
