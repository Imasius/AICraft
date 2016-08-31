package nimo.aic.tiles;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAIChest extends TileEntityId implements AIStorage {

    public static final int SIZE = 27;

    public ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            TileEntityAIChest.this.markDirty();
        }
    };

    @Override
    protected void readCustomNBT(NBTTagCompound root) {
        super.readCustomNBT(root);
        if (root.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) root.getTag("items"));
        }
    }

    @Override
    protected void writeCustomNBT(NBTTagCompound root) {
        super.writeCustomNBT(root);
        root.setTag("items", itemStackHandler.serializeNBT());
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemStackHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public IItemHandler getItemHandler() {
        return itemStackHandler;
    }
}
