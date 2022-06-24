package io.github.ramboxeu.techworks.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.BaseScreen;
import io.github.ramboxeu.techworks.client.screen.widget.inventory.SlotWidget;
import io.github.ramboxeu.techworks.client.screen.widget.progress.BurningProgressWidget;
import io.github.ramboxeu.techworks.client.util.ClientUtils;
import io.github.ramboxeu.techworks.common.heat.SolidFuelHeater;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SolidFuelHeatingWidget extends BaseContainerWidget implements IScreenWidgetProvider<SolidFuelHeatingWidget.ScreenWidget> {
    private final int x;
    private final int y;
    private final SolidFuelHeater heater;

    private ScreenWidget screenWidgetInstance;

    public SolidFuelHeatingWidget(SolidFuelHeater heater, int x, int y) {
        this.x = x;
        this.y = y;
        this.heater = heater;
    }

    @Override
    public void init(BaseContainer container, Builder builder) {
        builder.subWidget(new BurningProgressWidget(x + 2, y + 1, true, heater::getBurnTime, heater::getElapsedTime));

        if (heater.getFuelInvData() == null || !(container instanceof BaseMachineContainer)) {
            builder.slot(new SlotItemHandler(heater.getFuelInv(), 0, x + 1, y + 17));
        } else {
            builder.subWidget(new SlotWidget((BaseMachineContainer<?>) container, x, y + 16, 0, false, heater.getFuelInvData()));
        }
    }

    @Nonnull
    @Override
    public ScreenWidget getScreenWidget(BaseScreen<?> screen) {
        if (screenWidgetInstance == null)
            screenWidgetInstance = new ScreenWidget(screen, x, y);

        return screenWidgetInstance;
    }

    public static class ScreenWidget extends BaseScreenWidget {

        public ScreenWidget(BaseScreen<?> screen, int x, int y) {
            super(screen, x, y, 18, 34);
        }

        @Override
        protected void renderBaseWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            minecraft.textureManager.bindTexture(ClientUtils.WIDGETS_TEX);
            blit(stack, x, y + 16, 18, 18, 0, 0, 18, 18, 32, 32);
            blit(stack, x + 2, y + 1, 13, 13, 18, 0, 13, 13, 32, 32);
        }
    }
}
