package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentStackHandler;
import io.github.ramboxeu.techworks.client.container.ComponentsContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ComponentsScreen extends ContainerScreen<ComponentsContainer> {
    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/components.png");
    public static final ResourceLocation SLOT_TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/slot/component.png");

    public ComponentsScreen(ComponentsContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override // ComponentScreen.drawGuiBackgroundLayer
    protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        field_230706_i_.textureManager.bindTexture(BACKGROUND_TEXTURE);
        int x = (field_230708_k_ - xSize) / 2;
        int y = (field_230709_l_ - ySize) / 2;
        func_238474_b_(stack, x, y, 0, 0, xSize, ySize);

        renderSlots(stack);
    }

    @Override // IRenderable.render
    public void func_230430_a_(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);
        super.func_230459_a_(stack, mouseX, mouseY);
    }

    @Override // ComponentScreen.drawGuiForegroundLayer
    protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
        super.func_230451_b_(stack, mouseX, mouseY);
    }

    private void renderSlots(MatrixStack stack) {
        field_230706_i_.textureManager.bindTexture(SLOT_TEXTURE);

        ComponentStackHandler components = container.getComponents();

        for (int i = 0; i < components.getSlots(); i++) {
            Slot componentSlot = container.getSlot(i);
            func_238464_a_(stack, (componentSlot.xPos - 1) + guiLeft, (componentSlot.yPos - 1) + guiTop, 0,0.0F, 0.0F, 18, 18, 18, 18);
            components.getSlotList().get(i).getTexture().ifPresent(texture -> {
                field_230706_i_.textureManager.bindTexture(texture);
                func_238464_a_(stack, componentSlot.xPos + guiLeft, componentSlot.yPos + guiTop, 0,0.0F, 0.0F, 16, 16, 16, 16);
                field_230706_i_.textureManager.bindTexture(SLOT_TEXTURE);
            });
        }
    }
}
