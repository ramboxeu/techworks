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

        burnProgress = addWidget(new BurnProgressWidget(80 ,37, true));
        waterTank = addWidget(new FluidTankWidget(49, 15));
        steamTank = addWidget(new FluidTankWidget(109, 15));

        waterTank.setMaxStorage(container.getWaterTankStorage());
        steamTank.setMaxStorage(container.getSteamTankStorage());
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        int cookTime = container.getCookTime();
        int burnTime = container.getBurnTime();

        if (cookTime > 0 && burnTime > 0) {
            burnProgress.setProgress((float) cookTime / burnTime);
        } else {
            burnProgress.setProgress(0);
        }

        waterTank.setStoredFluid(container.getWaterStack());
        steamTank.setStoredFluid(container.getSteamStack());
        waterTank.setMaxStorage(container.getWaterTankStorage());
        steamTank.setMaxStorage(container.getSteamTankStorage());

        super.render(stack, mouseX, mouseY, partialTicks);
    }

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
