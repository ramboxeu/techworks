package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.BlueprintTableContainer;
import io.github.ramboxeu.techworks.client.util.Color;
import io.github.ramboxeu.techworks.common.item.BlueprintItem;
import io.github.ramboxeu.techworks.common.network.CBlueprintCraftPacket;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
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
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.ramboxeu.techworks.common.registration.TechworksItems.BLUEPRINTS;

public class BlueprintTableScreen extends BaseScreen<BlueprintTableContainer> {
    public static final ResourceLocation TEXTURE = guiTexture("blueprint_table");
    private final List<BlueprintButton> buttons = new ArrayList<>(BLUEPRINTS.size());
    private List<BlueprintButton> visibleButtons;
    private TextFieldWidget searchBar;
    private int offset = 0;
    private int scrollStep;
    private boolean isScrollPressed = false;
    private int lastGuiTop;
    private int lastGuiLeft;

    public BlueprintTableScreen(BlueprintTableContainer screenContainer, PlayerInventory inv, ITextComponent title) {
        super(screenContainer, inv, title, TEXTURE);

        ySize = 216;
        field_238745_s_ = ySize - 94; // Recalculate players inv title y with the new ySize

        for (int i = 0; i < BLUEPRINTS.size(); i++) {
            buttons.add(0, new BlueprintButton(
                    this::onBlueprintButtonPressed,
                    (BlueprintItem) BLUEPRINTS.get(i).getItem(),
                    i
            ));
        }
    }

    @Override
    protected void init() {
        super.init();

        visibleButtons = buttons;
        scrollStep = 76 / (visibleButtons.size() - 3);
        addBlueprintButtons();

        searchBar = addButton(new TextFieldWidget(font, 62 + guiLeft, 19 + guiTop, 106, 20, StringTextComponent.EMPTY));

        searchBar.setResponder(search -> {
            Techworks.LOGGER.debug("Search: {}", search);

            removeBlueprintButtons();
            // yep...
            visibleButtons = buttons.stream().filter(button -> button.label.toLowerCase().contains(search.toLowerCase())).collect(Collectors.toList());
            addBlueprintButtons();
            scrollStep = visibleButtons.size() > 0 ? 76 / (visibleButtons.size() - 3) : 0;
        });
    }

    @Override
    public void tick() {
        super.tick();

        // Get the button int the correct place after resize
        // This should instead use matrix stack translation powers, but that's for another time
        if (lastGuiLeft != guiLeft || lastGuiTop != guiTop) {
            lastGuiLeft = guiLeft;
            lastGuiTop = guiTop;

            setButtonPositions();
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);

        boolean flag = visibleButtons.size() > 4;

        for (int i = 0; i < (flag ? 4 : visibleButtons.size()); i++) {
            int index = i;

            if (flag) {
                index += offset;
            }

            visibleButtons.get(index).render(stack, mouseX, mouseY, partialTicks);
        }

        minecraft.textureManager.bindTexture(TEXTURE);

        if (isScrollPressed) {
            blit(stack, guiLeft + 162, guiTop + 41 + (offset * scrollStep), 182, 0, 6, 27);
        } else {
            blit(stack, guiLeft + 162, guiTop + 41 + (offset * scrollStep), 176, 0, 6, 27);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double horizontal, double vertical) {
        // button: 0 left click, 1 right click, 2 middle click

        int x = (int) (mouseX - guiLeft);
        int y = (int) (mouseY - guiTop);

        int barY = 41 + (offset * scrollStep);

        if (horizontal <= 0.5 && x >= 162 && x <= 168 && y >= barY && y <= barY + 27) {
            if (vertical > 0) { // dragged down
                if (offset < visibleButtons.size() - 4) {
                    offset++;
                    setButtonPositions();
                    return true;
                }
            } else { // dragged up
                if (offset > 0) {
                    offset--;
                    setButtonPositions();
                    return true;
                }
            }
        }

        return super.mouseDragged(mouseX, mouseY, button, horizontal, horizontal);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (int) (mouseX - guiLeft);
        int y = (int) (mouseY - guiTop);

        int barY = (guiTop + 41 + (offset * scrollStep));

        if (x >= 162 && x <= 168 && mouseY >= barY && mouseY <= barY + 27) {
            isScrollPressed = true;
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isScrollPressed) {
            isScrollPressed = false;
            return true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
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

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        int x = (int) (mouseX - guiLeft);
        int y = (int) (mouseY - guiTop);

        if (x >= 62 && y >= 41 && x <= 168 && y <= 121) {
            if (scroll > 0) { // wheel moved up
                if (offset > 0) {
                    offset--;
                    setButtonPositions();
                    return true;
                }
            } else { // wheel moved down
                if (offset < visibleButtons.size() - 4) {
                    offset++;
                    setButtonPositions();
                    return true;
                }
            }
        }

        return false;
    }

    private void onBlueprintButtonPressed(Button button) {
        if (button instanceof BlueprintButton) {
            BlueprintButton blueprint = (BlueprintButton) button;
            TechworksPacketHandler.sendBlueprintCraftPacket(new CBlueprintCraftPacket(ForgeRegistries.ITEMS.getKey(blueprint.blueprint)));
        }
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

    private void setButtonPositions() {
        boolean flag = visibleButtons.size() > 4;
        for (int i = 0; i < (flag ? 4 : visibleButtons.size()); i++) {
            int index = i;

            if (flag) {
                index += offset;
            }

            visibleButtons.get(index).setPos(guiLeft + 62, guiTop + 41 + (20 * i));
        }
    }

    public static class BlueprintButton extends Button {
        private final ItemStack machine;
        private final BlueprintItem blueprint;
        private final String label;
        private final String trimmedLabel;
        private final int index;

        public BlueprintButton(IPressable pressable, BlueprintItem blueprint, int index) {
            super(0, 0, 99, 20, StringTextComponent.EMPTY, pressable);

            Item machineItem = blueprint.getMachine();
            this.machine = new ItemStack(machineItem);
            this.index = index;
            this.blueprint = blueprint;

            label = machineItem.getName().getString();
            trimmedLabel = machineItem.getName().getStringTruncated(14);
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
