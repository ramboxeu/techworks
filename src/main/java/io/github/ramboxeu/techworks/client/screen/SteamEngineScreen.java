package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.SteamEngineContainer;
import io.github.ramboxeu.techworks.common.util.inventory.RenderUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;

public class SteamEngineScreen extends ContainerScreen<SteamEngineContainer> {
    public static final ResourceLocation BOILER_GUI_TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/steam_engine.png");

    public SteamEngineScreen(SteamEngineContainer container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderTanks();
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    private void renderTanks() {
        RenderUtils.drawGasInTank(this.guiLeft + 50,this.guiTop + 16, container.getGas(), new Color(195, 195, 195, 255), 16, 54, 10000);
        RenderUtils.drawEnergyInStorage(this.guiLeft + 110,this.guiTop + 16, container.getEnergy(), 16, 54, 10000);
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
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), (float) ((this.xSize / 2) - (this.font.getStringWidth(this.title.getString()) / 2)) , 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        int x = mouseX - this.guiLeft;
        int y = mouseY - this.guiTop;

        if (x >= 50 && x <= 65 && y >= 15 && y <= 68) {
            if (Screen.hasShiftDown()) {
                this.renderTooltip(String.format("Steam %db", container.getGas() / 1000), mouseX, mouseY);
            } else {
                this.renderTooltip(String.format("Steam %dmb", container.getGas()), mouseX, mouseY);
            }
        }

        if (x >= 100 && x <= 125 && y >= 15 && y <= 68) {
            if (Screen.hasShiftDown()) {
                this.renderTooltip(String.format("%s kFE", container.getEnergy() / 1000), mouseX, mouseY);
            } else {
                this.renderTooltip(String.format("%s FE", container.getEnergy()), mouseX, mouseY);
            }
        }

        super.renderHoveredToolTip(mouseX, mouseY);
    }
}
