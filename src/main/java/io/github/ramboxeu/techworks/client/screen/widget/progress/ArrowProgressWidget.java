package io.github.ramboxeu.techworks.client.screen.widget.progress;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.screen.BaseScreen;
import io.github.ramboxeu.techworks.client.screen.widget.BaseContainerWidget;
import io.github.ramboxeu.techworks.client.screen.widget.BaseScreenWidget;
import io.github.ramboxeu.techworks.client.screen.widget.IScreenWidgetProvider;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.ResourceLocation;

import java.util.function.IntSupplier;

public class ArrowProgressWidget extends BaseContainerWidget implements IScreenWidgetProvider<ArrowProgressWidget.ScreenWidget> {
    private final IntSupplier timeSupplier;
    private final IntSupplier elapsedTimeSupplier;
    private final boolean reversed;

    private int x;
    private int y;
    private int time;

    private ScreenWidget screenWidgetInstance;

    public ArrowProgressWidget(int x, int y, boolean reversed, IntSupplier timeSupplier, IntSupplier elapsedTimeSupplier) {
        this.x = x;
        this.y = y;
        this.timeSupplier = timeSupplier;
        this.elapsedTimeSupplier = elapsedTimeSupplier;
        this.reversed = reversed;
    }

    @Override
    public ScreenWidget getScreenWidget(BaseScreen<?> screen) {
        if (screenWidgetInstance == null) {
            screenWidgetInstance = new ScreenWidget(screen, x, y, reversed);
        }

        return screenWidgetInstance;
    }

    public boolean isReversed() {
        return screenWidgetInstance.reversed;
    }

    public void setReversed(boolean reversed) {
        screenWidgetInstance.reversed = reversed;
    }

    @Override
    public void init(BaseContainer container, Builder builder) {
        builder.track(new IntReferenceHolder() {
            @Override
            public int get() {
                return timeSupplier.getAsInt();
            }

            @Override
            public void set(int value) {
                time = value;
            }
        });

        builder.track(new IntReferenceHolder() {
            @Override
            public int get() {
                return elapsedTimeSupplier.getAsInt();
            }

            @Override
            public void set(int value) {
                screenWidgetInstance.progress = Utils.calcProgress(value, time);
            }
        });
    }

    public static class ScreenWidget extends BaseScreenWidget {
        private static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/widget/progress.png");

        private float progress;
        private boolean reversed;

        public ScreenWidget(BaseScreen<?> screen, int x, int y, boolean reversed) {
            super(screen, x, y, 22, 16);

            this.reversed = reversed;
        }

        @Override
        protected void renderWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            int texWidth = Math.round(width * progress);

            if (reversed) {
                texWidth = width - texWidth;
            }

            minecraft.textureManager.bindTexture(TEX);
            blit(stack, x, y - 1, 22, 0, texWidth, 16, 44, 16);
        }
    }
}
