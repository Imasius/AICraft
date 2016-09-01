package nimo.aic.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import nimo.aic.tiles.container.ContainerAIChest;


public class GuiAIChest extends GuiContainer {

    private static final ResourceLocation background = new ResourceLocation("textures/gui/container/generic_54.png");

    public GuiAIChest(ContainerAIChest container) {
        super(container);

        this.ySize = 114 + 3 * 18;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, 3 * 18 + 17);
        this.drawTexturedModalRect(i, j + 3 * 18 + 17, 0, 126, this.xSize, 96);
    }
}
