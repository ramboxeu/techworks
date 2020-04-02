package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.BoilerContainer;
import io.github.ramboxeu.techworks.common.util.inventory.RenderUtils;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class BoilerScreen extends ContainerScreen<BoilerContainer> {
    public static final ResourceLocation BOILER_GUI_TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/boiler.png");

    private BoilerContainer boilerContainer;

    public BoilerScreen(BoilerContainer boilerContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(boilerContainer, playerInventory, title);

        this.boilerContainer = boilerContainer;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        //this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        super.render(mouseX, mouseY, partialTicks);
        this.renderTanks();
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    private void renderTanks() {
        RenderUtils.drawFluid(this.guiLeft + 50,this.guiTop + 16, boilerContainer.getFluid(), 16, 54, 10000);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(BOILER_GUI_TEXTURE);
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
        // Water  | Steam
        // 50, 15 | 110, 15
        // 65, 68 | 125, 68
        int x = mouseX - this.guiLeft;
        int y = mouseY - this.guiTop;

        if (x >= 50 && x <= 65 && y >= 15 && y <= 68) {
            this.renderTooltip(String.format("Water %d m\u00B3", boilerContainer.getFluid().getAmount()), mouseX, mouseY);
        }

        if (x >= 100 && x <= 125 && y >= 15 && y <= 68) {
            this.renderTooltip(String.format("Steam %s m\u00B3", 0), mouseX, mouseY);
        }

        super.renderHoveredToolTip(mouseX, mouseY);
    }
}
