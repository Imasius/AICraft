package nimo.aic.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import nimo.aic.AICraft;
import nimo.aic.tiles.container.ContainerStorage;


public class GuiStorage extends GuiContainer {

    public static final int WIDTH = 176;
    public static final int HEIGHT = 88;

    private static final ResourceLocation background = new ResourceLocation(AICraft.MODID, "textures/gui/storage.png");

    public GuiStorage(ContainerStorage container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
