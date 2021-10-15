package io.github.ramboxeu.techworks.client.screen.machine;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.MetalPressContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.tile.machine.MetalPressTile;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;

public class MetalPressScreen extends BaseMachineScreen<MetalPressTile, MetalPressContainer> {
    public static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/metal_press.png");
    public static final ResourceLocation MACHINE_FRONT = new ResourceLocation(Techworks.MOD_ID, "block/metal_press_front_off");
    private static final ResourceLocation ICONS = new ResourceLocation(Techworks.MOD_ID , "textures/gui/widget/metal_press_mode_icons.png");

    public MetalPressScreen(MetalPressContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, TEX, MACHINE_FRONT);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int x, int y) {
        super.drawGuiContainerForegroundLayer(stack, x, y);

        minecraft.textureManager.bindTexture(ICONS);
        blit(stack, 83, 56, 16, 16, container.getMachineTile().getMode().getIconOffset(), 0, 16, 16, 32, 16);
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack stack, int mouseX, int mouseY) {
        super.renderHoveredTooltip(stack, mouseX, mouseY);

        int x = mouseX - guiLeft;
        int y = mouseY - guiTop;

        if (x >= 83 && y >= 56 && x <= 99 && y <= 72) {
            renderTooltip(stack, TranslationKeys.MODE.text().appendString(": ").appendSibling(container.getMachineTile().getMode().getName().text()), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (int) mouseX - guiLeft;
        int y = (int) mouseY - guiTop;

        if (x >= 83 && y >= 56 && x <= 99 && y <= 72) {
            minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            container.getMachineTile().cycleMode();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
