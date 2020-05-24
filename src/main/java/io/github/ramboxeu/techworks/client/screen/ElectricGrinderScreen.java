package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.ElectricGrinderContainer;
import io.github.ramboxeu.techworks.common.util.inventory.RenderUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ElectricGrinderScreen extends ContainerScreen<ElectricGrinderContainer> {
    public static final ResourceLocation ELECTRIC_GRINDER_GUI_TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/electric_grinder.png");

    public ElectricGrinderScreen(ElectricGrinderContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderTanks();
        this.renderProgressBars();
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    private void renderProgressBars() {
        this.minecraft.getTextureManager().bindTexture(ELECTRIC_GRINDER_GUI_TEXTURE);
        int workTime = container.getWorkTime();
        int workCounter = container.getWorkCounter();

        if (workTime > 0 && workCounter > 0) {
            int progress = (int) (24 * ((float) workCounter / workTime));

            this.blit(85 + this.guiLeft, 34 + this.guiTop, 176, 0, progress, 17);
        }
    }

    private void renderTanks() {
        RenderUtils.drawEnergyInStorage(this.guiLeft + 9, this.guiTop + 15, container.getEnergy(), 16, 54, 5000);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(ELECTRIC_GRINDER_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        this.font.drawString(this.title.getFormattedText(), (float) ((this.xSize / 2) - (this.font.getStringWidth(this.title.getString()) / 2)) , 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        int x = mouseX - this.guiLeft;
        int y = mouseY - this.guiTop;

        if (x >= 9 && x <= 25 && y >= 15 && y <= 68) {
            if (Screen.hasShiftDown()) {
                this.renderTooltip(String.format("%d kFE", container.getEnergy() / 1000), mouseX, mouseY);
            } else {
                this.renderTooltip(String.format("%d FE", container.getEnergy()), mouseX, mouseY);
            }
        }

        super.renderHoveredToolTip(mouseX, mouseY);
    }
}
