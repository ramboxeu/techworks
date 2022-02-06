package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.OreMinerContainer;
import io.github.ramboxeu.techworks.client.screen.machine.OreMinerScreen;
import io.github.ramboxeu.techworks.client.util.ClientUtils;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.tile.OreMinerTile;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class OreFilterConfigScreen extends NavigableScreen {
    public static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/ore_filter_config.png");

    private OreMinerScreen screen;

    private Button openStackConfig;
    private Button openTagConfig;
    private Button close;

    public OreFilterConfigScreen(OreMinerScreen previous) {
        super(TranslationKeys.CONFIGURE.text(), previous, TEX);
        screen = previous;
    }

    @Override
    protected void init() {
        super.init();
        openStackConfig = new Button(guiLeft + 18, guiTop + 46, 140, 20, TranslationKeys.ITEM_STACK.text(), this::onItemStackPressed, this::onItemStackTooltip);
        openTagConfig = new Button(guiLeft + 18, guiTop + 66, 140, 20, TranslationKeys.TAG.text(), this::onTagPressed, this::onTagTooltip);
        close = new Button(guiLeft + 18, guiTop + 86, 140, 20, TranslationKeys.CLEAR.text(), this::onClearPressed, this::onClearTooltip);
        addButton(openStackConfig);
        addButton(openTagConfig);
        addButton(close);
    }

    private void onItemStackPressed(Button button) {
        ClientUtils.changeCurrentScreen(this, new ItemScreen(screen, this), false);
    }

    private void onTagPressed(Button button) {
        ClientUtils.changeCurrentScreen(this, new TagScreen(screen, this), false);
    }

    private void onClearPressed(Button button) {
        screen.clearFilter();
        onGoBackPressed(back);
    }

    private void onItemStackTooltip(Button button, MatrixStack stack, int mouseX, int mouseY) {
        renderTooltip(stack, TranslationKeys.SET_ORE_FILTER_STACK.text(), mouseX, mouseY);
    }

    private void onTagTooltip(Button button, MatrixStack stack, int mouseX, int mouseY) {
        renderTooltip(stack, TranslationKeys.SET_ORE_FILTER_TAG.text(), mouseX, mouseY);
    }

    private void onClearTooltip(Button button, MatrixStack stack, int mouseX, int mouseY) {
        renderTooltip(stack, TranslationKeys.CLEAR_ORE_FILTER.text(), mouseX, mouseY);
    }

    public static class TagScreen extends NavigableScreen {
        public static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/ore_filter_config.png");
        private static final Style ERROR = Style.EMPTY.setBold(true).setFormatting(TextFormatting.DARK_RED);
        private static final Style NORMAL = Style.EMPTY.setFormatting(TextFormatting.GRAY);

        private final OreMinerScreen screen;
        private Button confirm;
        private TextFieldWidget search;
        private boolean invalid;

        public TagScreen(OreMinerScreen screen, Screen previous) {
            super(TranslationKeys.CONFIGURE.text(), previous, TEX);
            this.screen = screen;
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            InputMappings.Input input = InputMappings.getInputByCode(keyCode, scanCode);

            if (keyCode != 256 && minecraft.gameSettings.keyBindInventory.isActiveAndMatches(input) && search.isFocused()) {
                return true;
            } else {
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        }

        @Override
        protected void renderForeground(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            super.renderForeground(stack, mouseX, mouseY, partialTicks);

            if (invalid) {
                ClientUtils.drawString(stack, font, TranslationKeys.INVALID_TAG.styledText(ERROR), 18, 26 + 1, false);
            } else {
                ClientUtils.drawString(stack, font, TranslationKeys.ENTER_TAG_NAME.styledText(NORMAL), 18, 26 + 1, false);
            }
        }

        @Override
        protected void init() {
            super.init();
            search = new TextFieldWidget(font, guiLeft + 18, guiTop + 26 + 14, 140, 20, StringTextComponent.EMPTY);
            search.setResponder(text -> { if (invalid) invalid = false; });
            screen.getFilterTagName();
            search.setText(screen.getFilterTagName());
            addButton(search);

            confirm = new Button(guiLeft + 63, guiTop + 106, 50, 20, TranslationKeys.CONFIRM.text(), this::onConfirmPressed);
            addButton(confirm);
        }

        private void onConfirmPressed(Button button) {
            if (!screen.setFilterTag(search.getText())) {
                invalid = true;
            } else {
                previous.onClose();
                ClientUtils.changeCurrentScreen(this, screen, true);
            }
        }
    }

    public static class ItemScreen extends NavigableContainerScreen<OreMinerContainer> {
        private static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/stack_ore_filter_config.png");

        private final OreMinerScreen screen;
        private Button confirm;
        private ItemStack filterStack;

        public ItemScreen(OreMinerScreen screen, Screen previous) {
            super(screen.container, screen.getPlayerInv(), TranslationKeys.CONFIGURE.text(), previous, TEX);
            this.screen = screen;

            OreMinerTile.IOreFilter filter = screen.container.getMachineTile().getFilter();

            if (filter != null && filter.type() == OreMinerTile.FilterType.BLOCK) filterStack = screen.getFilterEntry();
            else filterStack = ItemStack.EMPTY;
        }

        @Override
        public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            super.render(stack, mouseX, mouseY, partialTicks);
        }

        @Override
        protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
            super.drawGuiContainerForegroundLayer(stack, mouseX, mouseY);
            itemRenderer.renderItemIntoGUI(filterStack, 80, 27);

            int x = mouseX - guiLeft;
            int y = mouseY - guiTop;

            if (x >= 80 && y >= 27 && x <= 96 && y <= 43) {
                RenderSystem.disableDepthTest();
                RenderSystem.colorMask(true, true, true, false);
                fill(stack, 80, 27, 96, 43, -2130706433);
                RenderSystem.colorMask(true, true, true, true);
                RenderSystem.enableDepthTest();

                if (!filterStack.isEmpty())
                    renderTooltip(stack, filterStack, x, y);
            }
        }

        @Override
        protected void init() {
            super.init();
            confirm = new Button(guiLeft + 63, guiTop + 50, 50, 20, TranslationKeys.CONFIRM.text(), this::onConfirmPressed);
            addButton(confirm);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == ClientUtils.LMB) {
                int x = (int) mouseX - guiLeft;
                int y = (int) mouseY - guiTop;

                if (x >= 80 && y >= 27 && x <= 96 && y <= 43) {
                    ItemStack mouseStack = screen.getPlayerInv().getItemStack();

                    if (!mouseStack.isEmpty()) {
                        Item item = mouseStack.getItem();

                        if (filterStack.getItem() != item && item instanceof BlockItem) {
                            filterStack = mouseStack.copy();
                            return true;
                        }
                    }
                }
            }

            return super.mouseClicked(mouseX, mouseY, button);
        }

        private void onConfirmPressed(Button button) {
            if (filterStack != screen.getFilterEntry())
                screen.setFilterItem(filterStack);

            previous.onClose();
            ClientUtils.changeCurrentScreen(this, screen, true);
        }
    }
}
