package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.IExtendedContainerListener;
import io.github.ramboxeu.techworks.client.container.machine.BoilerContainer;
import io.github.ramboxeu.techworks.client.screen.widget.BurnProgressWidget;
import io.github.ramboxeu.techworks.client.screen.widget.FluidTankWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BoilerScreen extends BaseScreen<BoilerContainer> {
    public static final ResourceLocation BOILER_GUI_TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/boiler.png");

    private final BurnProgressWidget burnProgress;
    private final FluidTankWidget waterTank;
    private final FluidTankWidget steamTank;

    public BoilerScreen(BoilerContainer boilerContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(boilerContainer, playerInventory, title, BOILER_GUI_TEXTURE);

        burnProgress = addWidget(new BurnProgressWidget(80 ,37));
        waterTank = addWidget(new FluidTankWidget(49, 15));
        steamTank = addWidget(new FluidTankWidget(109, 15));

        waterTank.setMaxStorage(5000);
        steamTank.setMaxStorage(5000);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        burnProgress.setProgress((float) container.getCookTime() / container.getBurnTime());
        waterTank.setStoredFluid(container.getWaterStack());
        steamTank.setStoredFluid(container.getSteamStack());

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    private void renderTanks() {
//        RenderUtils.drawFluidInTank(this.guiLeft + 50,this.guiTop + 16, boilerContainer.getFluid(), 16, 54, 10000);
//        RenderUtils.drawGasInTank(this.guiLeft + 110,this.guiTop + 16, boilerContainer.getGas(), new Color(195, 195, 195, 255), 16, 54, 10000);
    }

    private void renderProgressBars() {
        // Render fuel burning
//        this.minecraft.getTextureManager().bindTexture(BOILER_GUI_TEXTURE);
//        int burnTime = this.boilerContainer.getBurnTime();
//        int cookTime = this.boilerContainer.getCookTime();
//
//        if (burnTime > 0 && cookTime > 0) {
//            int progress = (int) (14 * ((float) cookTime / burnTime));
//
//            this.blit(80 + this.guiLeft, 37 + this.guiTop + progress, 176, progress, 14, 14);
//        }
    }

//    @Override
//    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        this.minecraft.getTextureManager().bindTexture(BOILER_GUI_TEXTURE);
//        int i = (this.width - this.xSize) / 2;
//        int j = (this.height - this.ySize) / 2;
//        this.blit(i, j, 0, 0, this.xSize, this.ySize);
//    }

//    @Override
//    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
//        this.font.drawString(this.title.getFormattedText(), (float) ((this.xSize / 2) - (this.font.getStringWidth(this.title.getString()) / 2)) , 6.0F, 4210752);
//        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
//    }
//
    @Override
    protected void renderHoveredTooltip(MatrixStack stack, int mouseX, int mouseY) {
        int x = mouseX - this.guiLeft;
        int y = mouseY - this.guiTop;

        if (x >= 80 && x <= 93 && y >= 37 && y <= 50) {
            this.renderTooltip(stack, container.getCookTime() + "/" + container.getBurnTime(), x, y);
        }

        super.renderHoveredTooltip(stack, mouseX, mouseY);
    }
}
