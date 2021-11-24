package io.github.ramboxeu.techworks.client.screen.widget;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.screen.BaseScreen;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.util.MathUtils;
import net.minecraft.util.IntReferenceHolder;

import javax.annotation.Nonnull;
import java.util.function.IntSupplier;

public class HeatDisplayWidget extends BaseContainerWidget implements IScreenWidgetProvider<HeatDisplayWidget.ScreenWidget> {
    private final int x;
    private final int y;
    private final int maxHeat;
    private final IntSupplier heatSupplier;
    private final IntSupplier tempSupplier;

    private ScreenWidget screenWidgetInstance;
    private int heat;
    private int temperature;

    public HeatDisplayWidget(int x, int y, int maxHeat, IntSupplier heatSupplier) {
        this(x, y, maxHeat, heatSupplier, null);
    }

    public HeatDisplayWidget(int x, int y, int maxHeat, IntSupplier heatSupplier, IntSupplier tempSupplier) {
        this.x = x;
        this.y = y;
        this.heatSupplier = heatSupplier;
        this.maxHeat = maxHeat;
        this.tempSupplier = tempSupplier;
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

        if (tempSupplier != null) {
            builder.track(new IntReferenceHolder() {
                @Override
                public int get() {
                    return tempSupplier.getAsInt();
                }

                @Override
                public void set(int value) {
                    temperature = value;
                    screenWidgetInstance.temp = MathUtils.calcProgress(value, maxHeat);
                }
            });
        }
    }

    @Nonnull
    @Override
    public ScreenWidget getScreenWidget(BaseScreen<?> screen) {
        if (screenWidgetInstance == null)
            screenWidgetInstance = new ScreenWidget(this, screen, x, y, tempSupplier != null);

        return screenWidgetInstance;
    }

    public static class ScreenWidget extends BaseScreenWidget {
        private final HeatDisplayWidget widget;
        private final boolean renderTemp;

        private float progress;
        private float temp;

        public ScreenWidget(HeatDisplayWidget widget, BaseScreen<?> screen, int x, int y, boolean renderTemp) {
            super(screen, x, y, 6, 56);
            this.widget = widget;
            this.renderTemp = renderTemp;
        }

        @Override
        protected void renderBaseWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            int y = (int) (this.y + (53 - (53 * progress))) + 1;
            fill(stack, x + 1, y, x + 5, y + 1, 0xFF4F4D4D);

            if (renderTemp) {
                int tempY = (int) (this.y + (53 - (53 * temp))) + 1;
                fill(stack, x + 1, tempY, x + 5, tempY + 1, 0xFF2D2F33);
            }
        }

        @Override
        protected void renderWidgetTooltip(MatrixStack stack, int mouseX, int mouseY) {
            if (renderTemp) {
                int y = (int) (this.y + (53 - (temp * 53))) + 1;

                if (mouseX >= x + 1 && mouseY >= y && mouseX <= x + 5 && mouseY <= y + 1) {
                    if (temp == progress) {
                        renderTooltip(stack, ImmutableList.of(TranslationKeys.TEMPERATURE.text(widget.temperature), TranslationKeys.HEAT_DISPLAY.text(widget.heat, widget.maxHeat)), mouseX, mouseY);
                    } else {
                        renderTooltip(stack, TranslationKeys.TEMPERATURE.text(widget.temperature), mouseX, mouseY);
                    }

                    return;
                }
            }

            renderTooltip(stack, TranslationKeys.HEAT_DISPLAY.text(widget.heat, widget.maxHeat), mouseX, mouseY);
        }
    }
}
