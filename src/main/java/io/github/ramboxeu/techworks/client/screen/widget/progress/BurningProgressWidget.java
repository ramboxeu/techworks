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

public class BurningProgressWidget extends BaseContainerWidget implements IScreenWidgetProvider<BurningProgressWidget.ScreenWidget> {
    private final IntSupplier timeSupplier;
    private final IntSupplier elapsedTmeSupplier;
    private final boolean reversed;

    private int x;
    private int y;
    private int time;
    private ScreenWidget screenWidgetInstance;

    public BurningProgressWidget(int x, int y, boolean reversed, IntSupplier timeSupplier, IntSupplier elapsedTmeSupplier) {
        this.timeSupplier = timeSupplier;
        this.elapsedTmeSupplier = elapsedTmeSupplier;
        this.x = x;
        this.y = y;
        this.reversed = reversed;
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
                return elapsedTmeSupplier.getAsInt();
            }

            @Override
            public void set(int value) {
                screenWidgetInstance.progress = Utils.calcProgress(value, time);
            }
        });
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

    public static class ScreenWidget extends BaseScreenWidget {
        private static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/widget/burn_progress.png");

        private float progress;
        private boolean reversed;

        public ScreenWidget(BaseScreen<?> screen, int x, int y, boolean reversed) {
            super(screen, x, y, 14, 14);

            this.reversed = reversed;
        }

        @Override
        protected void renderWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            int height = Math.round(this.height * progress);
            int texHeight = this.height - height;

            minecraft.textureManager.bindTexture(TEX);
            blit(stack, x - 1, y - 1 + texHeight, width, height, 14, texHeight, 14, height, 28, 14);
        }
    }
}
