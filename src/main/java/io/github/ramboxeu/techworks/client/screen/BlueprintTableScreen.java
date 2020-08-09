package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.BlueprintTableContainer;
import io.github.ramboxeu.techworks.client.util.Color;
import io.github.ramboxeu.techworks.common.item.BlueprintItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.ramboxeu.techworks.common.registration.TechworksItems.BLUEPRINTS;

public class BlueprintTableScreen extends BaseScreen<BlueprintTableContainer> {
    public static final ResourceLocation TEXTURE = guiTexture("blueprint_table");
    private final List<BlueprintButton> buttons = new ArrayList<>(BLUEPRINTS.size());
    private List<BlueprintButton> visibleButtons;
    private TextFieldWidget searchBar;

    public BlueprintTableScreen(BlueprintTableContainer screenContainer, PlayerInventory inv, ITextComponent title) {
        super(screenContainer, inv, title, TEXTURE);

        ySize = 216;
        field_238745_s_ = ySize - 94; // Recalculate players inv title y with the new ySize

        for (int i = 0; i < BLUEPRINTS.size(); i++) {
            buttons.add(0, new BlueprintButton(
                    btn -> onBlueprintButtonPressed((BlueprintButton) btn),
                    ((BlueprintItem) BLUEPRINTS.get(i).getItem()).getMachine(),
                    i
            ));
        }
    }

    @Override
    protected void init() {
        super.init();

        visibleButtons = buttons.subList(0, 4);
        addBlueprintButtons();

        searchBar = addButton(new TextFieldWidget(font, 62 + guiLeft, 19 + guiTop, 106, 20, StringTextComponent.EMPTY));

        searchBar.setResponder(search -> {
            Techworks.LOGGER.debug("Search: {}", search);

            removeBlueprintButtons();
            // yep...
            visibleButtons = buttons.stream().filter(button -> button.label.toLowerCase().contains(search.toLowerCase())).collect(Collectors.toList());
            addBlueprintButtons();
        });
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);

        for (int i = 0; i < Math.min(4, visibleButtons.size()); i++) {
            visibleButtons.get(i).render(stack, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int p_231046_3_) {
        InputMappings.Input input = InputMappings.getInputByCode(keyCode, scanCode);

        // Avoid closing gui when typing in search bar
        if (keyCode != 256 && minecraft.gameSettings.keyBindInventory.isActiveAndMatches(input) && searchBar.isFocused()) {
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, p_231046_3_);
        }
    }

    private void onBlueprintButtonPressed(BlueprintButton button) {
        Techworks.LOGGER.debug("Blueprint id: {}", button.index);
    }


    private void removeBlueprintButtons() {
        for (BlueprintButton button : visibleButtons) {
            super.buttons.remove(button);
        }
    }

    private void addBlueprintButtons() {
        for (int i = 0; i < visibleButtons.size(); i++) {
            BlueprintButton button = visibleButtons.get(i);
            button.setPos(guiLeft + 62, guiTop + 41 + (20 * i));
            addListener(button);
        }
    }

    public static class BlueprintButton extends Button {
        private final ItemStack machine;
        private final String label;
        private final String trimmedLabel;
        private final int index;

        public BlueprintButton(IPressable pressable, Item machine, int index) {
            super(0, 0, 99, 20, StringTextComponent.EMPTY, pressable);

            this.machine = new ItemStack(machine);
            this.index = index;

            label = machine.getName().getString();
            trimmedLabel = machine.getName().getStringTruncated(14);
        }

        public void setPos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        protected void renderBg(MatrixStack stack, Minecraft minecraft, int mouseX, int mouseY) {
            minecraft.getItemRenderer().renderItemIntoGUI(machine, this.x + 80, this.y + 2);
            minecraft.fontRenderer.drawString(stack, trimmedLabel, this.x + 3, this.y + 4, Color.toRGBA(255, 255, 255, 255));
        }
    }
}
