package nimo.aic.tiles;

import net.minecraft.nbt.NBTTagCompound;


public class TileEntityController extends TileEntityBase {

    private String name = "";

    @Override
    protected void readCustomNBT(NBTTagCompound root) {
        name = root.getString("name");
    }

    @Override
    protected void writeCustomNBT(NBTTagCompound root) {
        root.setString("name", name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        markDirty();
        updateBlock(7);
    }
}
