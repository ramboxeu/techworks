package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.widget.Widget;
import io.github.ramboxeu.techworks.common.container.machine.BoilerContainer;
import io.github.ramboxeu.techworks.common.registry.TechworksContainers;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;

public class BoilerScreen extends ContainerScreen<BoilerContainer> {
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

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.drawBackground(delta, mouseX, mouseY);
        super.render(mouseX, mouseY, delta);
    }

    @Override
    protected void drawBackground(float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(BOILER_SCREEN);
        int i = (this.width - this.containerWidth) / 2;
        int j = (this.height - this.containerHeight) / 2;
        this.blit(i, j, 0, 0, this.containerWidth, this.containerHeight);
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        for (Widget widget : widgetList) {
            widget.render(mouseX, mouseY, this, minecraft, container);
        }
    }

    @Override
    protected void drawMouseoverTooltip(int mouseX, int mouseY) {

    }
}
