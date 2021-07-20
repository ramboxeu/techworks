package io.github.ramboxeu.techworks.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.screen.BaseScreen;
import io.github.ramboxeu.techworks.common.util.MathUtils;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.function.IntSupplier;

public class HeatDisplayWidget extends BaseContainerWidget implements IScreenWidgetProvider<HeatDisplayWidget.ScreenWidget> {
    private final int x;
    private final int y;
    private final int maxHeat;
    private final IntSupplier heatSupplier;

    private ScreenWidget screenWidgetInstance;
    private int heat;

    public HeatDisplayWidget(int x, int y, int maxHeat, IntSupplier heatSupplier) {
        this.x = x;
        this.y = y;
        this.heatSupplier = heatSupplier;
        this.maxHeat = maxHeat;
    }

    @Override
    public void init(BaseContainer container, Builder builder) {
        builder.track(new IntReferenceHolder() {
            @Override
            public int get() {
                return heatSupplier.getAsInt();
            }

            @Override
            public void set(int value) {
                heat = value;
                screenWidgetInstance.progress = MathUtils.calcProgress(value, maxHeat);
            }
        });
    }

    @Nonnull
    @Override
    public ScreenWidget getScreenWidget(BaseScreen<?> screen) {
        if (screenWidgetInstance == null)
            screenWidgetInstance = new ScreenWidget(this, screen, x, y);

        return screenWidgetInstance;
    }

    public static class ScreenWidget extends BaseScreenWidget {
        private final HeatDisplayWidget widget;

        private float progress;

        public ScreenWidget(HeatDisplayWidget widget, BaseScreen<?> screen, int x, int y) {
            super(screen, x, y, 6, 56);
            this.widget = widget;
        }

        @Override
        protected void renderBaseWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            int y = (int) (this.y + (53 - (53 * progress))) + 1;

            fill(stack, x + 1, y, x + 5, y + 1, 0xFF4F4D4D);
        }

        @Override
        protected void renderWidgetTooltip(MatrixStack stack, int mouseX, int mouseY) {
            renderTooltip(stack, new TranslationTextComponent("tooltip.techworks.widget.heat_display_heat", widget.heat, widget.maxHeat), mouseX, mouseY);
        }
    }
}
