package io.github.ramboxeu.techworks.client.screen.widget.config;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.client.screen.widget.BaseContainerWidget;
import io.github.ramboxeu.techworks.client.util.ClientUtils;
import io.github.ramboxeu.techworks.common.component.Component;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static io.github.ramboxeu.techworks.client.util.ClientUtils.GUI_WIDTH;

public class ComponentsWidget extends BaseContainerWidget {

    private final ComponentStorage storage;
    private final List<ComponentType<?>> typeLookup;
    private BaseContainer.ToggleableSlot slot;
    private BaseContainer container;
    private ScreenWidget widgetInstance;

    public ComponentsWidget(ComponentStorage storage) {
        this.storage = storage;
        typeLookup = new ArrayList<>(storage.getSupportedTypes());
    }

    public ScreenWidget getScreenWidget(BaseMachineScreen<?, ?> screen) {
        if (widgetInstance == null) {
            widgetInstance = new ScreenWidget(screen, this);
        }

        return widgetInstance;
    }

    @Override
    public void init(BaseContainer container, Builder builder) {
        this.container = container;

        slot = builder.toggleableSlot(new SlotItemHandler(storage, 0, GUI_WIDTH + 7, 140) {
            @Override
            public void putStack(@Nonnull ItemStack stack) {
                storage.insertItem(0, stack, false);
            }
        });

        slot.setEnabled(false);
    }

    @Override
    public void onButtonClicked(int buttonId) {
        ComponentType<?> type = typeLookup.get(buttonId);

        if (type != null) {
            storage.uninstall(type);
        }
    }

    public static class ScreenWidget extends BaseConfigWidget {

        private final ComponentsWidget widget;

        public ScreenWidget(BaseMachineScreen<?, ?> screen, ComponentsWidget widget) {
            super(screen, ClientUtils.processor("Components"));
            this.widget = widget;
        }

        @Override
        protected void renderConfig(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            blit(stack, 6, 139, 185, 9, 18, 18);

            List<Component> components = widget.storage.getComponents();

            for (int i = 0, len = components.size(); i < len && i < 6; i++) {
                Component component = components.get(i);

                fill(stack, 6, 9 + (22 * i), 115 + 6, 9 + 18 + (22 * i), 0xFF2B2828);
                minecraft.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(component.getItem()), guiLeft + 7, guiTop + 10 + (i * 22));
                ClientUtils.drawString(stack, minecraft.fontRenderer, component.getName().getStringTruncated(14), 26, 13.5f + (22 * i), 0xFF7F7F7F, false);

                minecraft.textureManager.bindTexture(GUI_LOCATION);
                blit(stack, 106, 12 + (22 * i), 155, 9, 12, 12);
            }

            switch (widget.storage.getOperation()) {
                case NONE:
                    ClientUtils.drawString(stack, minecraft.fontRenderer, "Idle", 30, 140f, 0xFF7F7F7F, false);
                    break;
                case INSTALL:
                    ClientUtils.drawString(stack, minecraft.fontRenderer, "Installing", 30, 140f, 0xFF7F7F7F, false);
                    break;
                case UNINSTALL:
                    ClientUtils.drawString(stack, minecraft.fontRenderer, "Uninstalling", 30, 140f, 0xFF7F7F7F, false);
                    break;
            }

            int x = 30;
            int y = 149;
            int width = 90;
            int height = 8;

            hLine(stack, x, x + width, y, 0xFF474545);
            hLine(stack, x, x + width, (y + height) - 1, 0xFF474545);
            vLine(stack, x, y, y + height, 0xFF474545);
            vLine(stack, x + width, y, y + height, 0xFF474545);

            if (widget.storage.isProcessing()) {
                int barWidth = (int) ((width - 2) * widget.storage.getOperationProgress());

                fill(stack, x + 1, y + 1, x + barWidth, (y + height) - 1, 0xFF2D9938);
                fill(stack, x + barWidth, y + 1, x + width, (y + height) - 1, 0xFF2B2828);
            } else {
                if (widget.storage.isFinished()) {
                    fill(stack, x + 1, y + 1, x + width, (y + height) - 1, 0xFF2D9938);
                } else {
                    fill(stack, x + 1, y + 1, x + width, (y + height) - 1, 0xFF2B2828);
                }
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            int x = (int) (mouseX - guiLeft);
            int y = (int) (mouseY - guiTop);

            List<Component> components = widget.storage.getComponents();
            for (int i = 0, len = components.size(); i < len; i++) {
                if (x >= 106 && y >= 12 + (22 * i) && x <= 106 + 12 && y <= (22 * i) + 24) {
                    if (!widget.storage.isProcessing()) {
                        widget.storage.uninstall(components.get(i).getType());
                        widget.container.buttonClicked(widget, i);
                    }

                    return true;
                }
            }

            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        protected void renderWidgetTooltip(MatrixStack stack, int x, int y) {
            super.renderWidgetTooltip(stack, x, y);

            List<Component> components = widget.storage.getComponents();

            for (int i = 0, len = components.size(); i < len; i++) {
//                if (x >= 26 && y >= 10 + (22 * i) && x <= 109 && y <= 26 + (22 * i)) {
                if (x >= 10 && y >= 10 + (22 * i) && x <= 109 && y <= 26 + (22 * i)) {
                    Component component = components.get(i);
                    List<ITextComponent> list = new ArrayList<>();

                    list.add(component.getName());
                    list.addAll(component.getTooltipInfo());

                    ClientUtils.renderTooltip(screen, minecraft.fontRenderer, stack, list, x, y);
                }
            }
        }

        @Override
        public void toggle() {
            super.toggle();
            widget.slot.setEnabled(render);
        }
    }
}
