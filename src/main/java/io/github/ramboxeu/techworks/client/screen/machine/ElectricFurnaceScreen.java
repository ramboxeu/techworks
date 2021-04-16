package io.github.ramboxeu.techworks.client.screen.machine;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.ElectricFurnaceContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.client.screen.widget.config.IOConfigWidget;
import io.github.ramboxeu.techworks.common.tile.machine.ElectricFurnaceTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ElectricFurnaceScreen extends BaseMachineScreen<ElectricFurnaceTile, ElectricFurnaceContainer> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/electric_furnace.png");
    public static final ResourceLocation MACHINE_FRONT = new ResourceLocation(Techworks.MOD_ID, "block/electric_furnace_front_off");

//    private final ProgressWidget progressBar;

    public ElectricFurnaceScreen(ElectricFurnaceContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn, TEXTURE, MACHINE_FRONT);

//        progressBar = addBaseWidget(new ProgressWidget(80, 34));
        addConfigWidget(new IOConfigWidget(this, MACHINE_FRONT));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(stack, mouseX, mouseY);
//        progressBar.setProgress((float) container.getWorkTime() / container.getCookTime());
//        battery.setEnergy(container.getEnergy());

//        drawCenteredString(stack, minecraft.fontRenderer, String.valueOf(container.getWorkTime()), 0, 0, 0xFFFFFF);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);

        int x = 0;
        int y = 0;
//        for (MachineIO.Metadata metadata : container.getMachineTile().getMetadataList()) {
//            x += 10;
//            fill(stack, x, y, x + 10, y + 10, metadata.getColor());
//        }
    }

    //    @Override
//    public void render(int mouseX, int mouseY, float partialTicks) {
//        this.renderBackground();
//        super.render(mouseX, mouseY, partialTicks);
//        this.renderTanks();
//        this.renderProgressBars();
//        this.renderHoveredToolTip(mouseX, mouseY);
//    }
//
//    private void renderProgressBars() {
//        this.minecraft.getTextureManager().bindTexture(ELECTRIC_FURNACE_GUI_TEXTURE);
//        int workTime = container.getWorkTime();
//        int workCounter = container.getWorkCounter();
//
//        if (workTime > 0 && workCounter > 0) {
//            int progress = (int) (24 * ((float) workCounter / workTime));
//
//            this.blit(80 + this.guiLeft, 34 + this.guiTop, 176, 0, progress, 17);
//        }
//    }
//
//    private void renderTanks() {
//        RenderUtils.drawEnergyInStorage(this.guiLeft + 9, this.guiTop + 15, container.getEnergy(), 16, 54, 5000);
//    }
//
//    @Override
//    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        this.minecraft.getTextureManager().bindTexture(ELECTRIC_FURNACE_GUI_TEXTURE);
//        int i = (this.width - this.xSize) / 2;
//        int j = (this.height - this.ySize) / 2;
//        this.blit(i, j, 0, 0, this.xSize, this.ySize);
//    }
//
//
//    @Override
//    protected void renderHoveredToolTip(int mouseX, int mouseY) {
//        int x = mouseX - this.guiLeft;
//        int y = mouseY - this.guiTop;
//
//        if (x >= 9 && x <= 25 && y >= 15 && y <= 68) {
//            if (Screen.hasShiftDown()) {
//                this.renderTooltip(String.format("%d kFE", container.getEnergy() / 1000), mouseX, mouseY);
//            } else {
//                this.renderTooltip(String.format("%d FE", container.getEnergy()), mouseX, mouseY);
//            }
//        }
//
//        super.renderHoveredToolTip(mouseX, mouseY);
//    }
}
