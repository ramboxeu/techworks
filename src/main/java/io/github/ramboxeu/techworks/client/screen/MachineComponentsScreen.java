package io.github.ramboxeu.techworks.client.screen;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.container.machine.MachineComponentsContainer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class MachineComponentsScreen extends HandledScreen<MachineComponentsContainer> {
    public static final Identifier BACKGROUND = new Identifier(Techworks.MOD_ID, "textures/gui/container/machine_components.png");
    private static final Identifier COMPONENT_SLOT_BG = new Identifier(Techworks.MOD_ID, "textures/gui/slot/component_slot.png");

    public MachineComponentsScreen(MachineComponentsContainer container) {
        super(container, container.getPlayerInventory(), container.getName());
    }

    // FIXME
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//        super.render(mouseX, mouseY, delta);
//        this.drawMouseoverTooltip(mouseX, mouseY);
    }

    // FIXME
    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        minecraft.getTextureManager().bindTexture(BACKGROUND);
//        int i = (this.width - this.containerWidth) / 2;
//        int j = (this.height - this.containerHeight) / 2;
//        this.blit(this.x, this.y, 0, 0, this.containerWidth, this.containerHeight);
//
//        for (ComponentSlot slot : container.getComponentSlotList()) {
//            int slotX = slot.xPosition + this.x;
//            int slotY = slot.yPosition + this.y;
////            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//            minecraft.getTextureManager().bindTexture(COMPONENT_SLOT_BG);
//            blit(slotX - 1, slotY - 1, 0, 0, 18, 18, 18, 18);
////            fill(slotX, slotY, slotX + 16, slotY + 16, 838860800);
//        }
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
//        String title = container.getName().asString();
//
//        font.draw(title, (float)(containerWidth / 2 - font.getStringWidth(title) / 2), 6.0F, 4210752);
//        font.draw(playerInventory.getDisplayName().asFormattedString(), 8.0F, (float)(containerHeight - 96 + 2), 4210752);
    }
}
