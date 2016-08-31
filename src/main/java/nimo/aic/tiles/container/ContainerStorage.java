package nimo.aic.tiles.container;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import nimo.aic.tiles.TileEntityStorage;

import javax.annotation.Nullable;

public class ContainerStorage extends Container {

    private final TileEntityStorage storageTE;

    public ContainerStorage(IInventory playerInventory, TileEntityStorage storageTE) {
        this.storageTE = storageTE;

        addOwnSlots();
    }

    private void addOwnSlots() {
        IItemHandler itemHandler = storageTE.itemStackHandler;
        int x = 8;
        int y = 18;

        int slotIndex = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
            slotIndex++;
            x += 18;
            if (x > 9 * 18) {
                x = 8;
                y += 18;
            }
        }
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        return null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return storageTE.canPlayerAccess(playerIn);
    }

    @Nullable
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        return null;
    }
}
