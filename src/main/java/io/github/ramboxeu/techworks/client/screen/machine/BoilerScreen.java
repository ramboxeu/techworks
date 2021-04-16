package io.github.ramboxeu.techworks.client.screen.machine;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.BoilerContainer;
import io.github.ramboxeu.techworks.client.screen.BaseScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BoilerScreen extends BaseScreen<BoilerContainer> {
    public static final ResourceLocation BOILER_GUI_TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/boiler.png");

    public BoilerScreen(BoilerContainer boilerContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(boilerContainer, playerInventory, title, BOILER_GUI_TEXTURE);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        int cookTime = container.getCookTime();
        int burnTime = container.getBurnTime();

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
