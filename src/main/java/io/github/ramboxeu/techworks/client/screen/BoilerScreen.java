package io.github.ramboxeu.techworks.client.screen;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.widget.Widget;
import io.github.ramboxeu.techworks.common.container.machine.BoilerContainer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;

public class BoilerScreen extends HandledScreen<BoilerContainer> {
    private List<Widget> widgetList;
    private static final Identifier COMPONENTS_SCREEN_BUTTON = new Identifier(Techworks.MOD_ID, "textures/gui/button/components_screen.png");
    private static final Identifier BOILER_SCREEN = new Identifier(Techworks.MOD_ID, "textures/gui/container/boiler.png");

    public BoilerScreen(BoilerContainer container) {
        super(container, container.getPlayerInventory(), new TranslatableText("screen.techworks.boiler"));

        widgetList = container.getWidgets();
    }

    @Override
    protected void init() {
//        this.addButton(new TexturedButtonWidget(10, 10, 16, 16, 0, 0, 16, COMPONENTS_SCREEN_BUTTON, 32, 16, button -> {
//            Techworks.LOG.info("Button clicked!");
//            ContainerProviderRegistry.INSTANCE.openContainer(TechworksContainers.MACHINE_COMPONENTS, this.playerInventory.player, buf -> buf.writeBlockPos(container.getBlockEntity().getPos()));
//        }));
    }

    // FIXME
    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        minecraft.getTextureManager().bindTexture(BOILER_SCREEN);
//        int i = (this.width - this.containerWidth) / 2;
//        int j = (this.height - this.containerHeight) / 2;
//        this.blit(i, j, 0, 0, this.containerWidth, this.containerHeight);
    }

    // FIXME
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//        this.drawBackground(delta, mouseX, mouseY);
//        super.render(mouseX, mouseY, delta);
    }

    // FIXME
    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
//        for (Widget widget : widgetList) {
//            widget.render(mouseX, mouseY, this, minecraft, container);
//        }
    }
}
