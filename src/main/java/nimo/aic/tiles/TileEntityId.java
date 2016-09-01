package nimo.aic.tiles;


import net.minecraft.nbt.NBTTagCompound;
import nimo.aic.AICraft;
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
        if (!initialized) {
            AICraft.proxy.ai.register(aiId, this);
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
        this.aiId = id;
        markDirty();
        updateBlock(7);
    }

    public Id getId() {
        return aiId;
    }
}
