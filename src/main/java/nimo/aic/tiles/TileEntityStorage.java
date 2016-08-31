package nimo.aic.tiles;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityStorage extends TileEntityId implements AIStorage {

    public static final int SIZE = 27;

    public ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            TileEntityStorage.this.markDirty();
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
    public IItemHandler getItemHandler() {
        return itemStackHandler;
    }
}
