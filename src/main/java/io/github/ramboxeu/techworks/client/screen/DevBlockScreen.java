package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.DevBlockContainer;
import io.github.ramboxeu.techworks.client.util.Utils;
import io.github.ramboxeu.techworks.common.tile.DevBlockTile;
import io.github.ramboxeu.techworks.common.util.Side;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.EnumSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.ramboxeu.techworks.common.tile.DevBlockTile.ActiveSignal;

public class DevBlockScreen extends ContainerScreen<DevBlockContainer> {
    private final Button increaseEnergyBtn;
    private final Button decreaseEnergyBtn;
    private final Button energyActiveSignalBtn;
    private final SideSelection energySideSelection;
    private TextFieldWidget energyTextField;
    private int energy;
    private ActiveSignal energyActiveSignal = ActiveSignal.NEVER;

    private final Button increaseLiquidBtn;
    private final Button decreaseLiquidBtn;
    private final Button liquidActiveSignalBtn;
    private final Button setLiquidBtn;
    private final SideSelection liquidSideSelection;
    private TextFieldWidget liquidTextField;
    private TextFieldWidget liquidIdTextField;
    private int liquidAmount;
    private Fluid liquid;
    private ActiveSignal liquidActiveSignal = ActiveSignal.NEVER;

    private final Button increaseGasBtn;
    private final Button decreaseGasBtn;
    private final Button gasActiveSignalBtn;
    private final Button setGasBtn;
    private final SideSelection gasSideSelection;
    private TextFieldWidget gasTextField;
    private TextFieldWidget gasIdTextField;
    private int gasAmount;
    private Fluid gas;
    private ActiveSignal gasActiveSignal = ActiveSignal.NEVER;

    private final Button setItemBtn;
    private final Button itemActiveSignalBtn;
    private final SideSelection itemSideSelection;
    private TextFieldWidget itemTextField;
    private TextFieldWidget itemIdTextField;
    private ActiveSignal itemActiveSignal = ActiveSignal.NEVER;

    private final Button nextPageBtn;
    private final Button prevPageBtn;
    private int page = 0;
    private List<String> logs;
    private List<ItemStack> inv;

    public DevBlockScreen(DevBlockContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);

        DevBlockTile tile = container.getTile();
        energy = tile.getEnergyPerTick();
        decreaseEnergyBtn = new Button(40, 50, 20, 20, new StringTextComponent("-"), this::decreaseEnergy);
        increaseEnergyBtn = new Button(166, 50, 20, 20, new StringTextComponent("+"), this::increaseEnergy);
        energyActiveSignal = tile.getEnergySignal();
        energyActiveSignalBtn = new Button(189, 50, 46, 20, new StringTextComponent(energyActiveSignal.name()), this::changeEnergyActiveSignal);
        energySideSelection = new SideSelection(238, 50, tile.getEnergySides(), (checkboxButton, side) -> syncEnergy());

        liquid = tile.getLiquid();
        liquidAmount = tile.getLiquidPerTick();
        decreaseLiquidBtn = new Button(40, 74, 20, 20, new StringTextComponent("-"), this::decreaseLiquid);
        increaseLiquidBtn = new Button(166, 74, 20, 20, new StringTextComponent("+"), this::increaseLiquid);
        liquidActiveSignal = tile.getLiquidActiveSignal();
        liquidActiveSignalBtn = new Button(189, 74, 46, 20, new StringTextComponent(liquidActiveSignal.name()), this::changeLiquidActiveSignal);
        setLiquidBtn = new Button(311, 74, 30, 20, new StringTextComponent("Set"), this::setLiquid);
        liquidSideSelection = new SideSelection(344, 74, tile.getLiquidSides(), (checkboxButton, side) -> syncLiquid());

        gas = tile.getGas();
        gasAmount = tile.getGasPerTick();
        decreaseGasBtn = new Button(40, 98, 20, 20, new StringTextComponent("-"), this::decreaseGas);
        increaseGasBtn = new Button(166, 98, 20, 20, new StringTextComponent("+"), this::increaseGas);
        gasActiveSignal = tile.getGasActiveSignal();
        gasActiveSignalBtn = new Button(189, 98, 46, 20, new StringTextComponent(gasActiveSignal.name()), this::changeGasActiveSignal);
        setGasBtn = new Button(311, 98, 30, 20, new StringTextComponent("Set"), this::setGas);
        gasSideSelection = new SideSelection(344, 98, tile.getGasSides(), (checkboxButton, side) -> syncGas());

        setItemBtn = new Button(199, 122, 30, 20, new StringTextComponent("Add"), this::setItem);
        itemActiveSignal = tile.getInvActiveSignal();
        itemActiveSignalBtn = new Button(232, 122, 46, 20, new StringTextComponent(itemActiveSignal.name()), this::changeItemActiveSignal);
        itemSideSelection = new SideSelection(281, 122, tile.getInvSides(), (checkboxButton, side) -> syncItems());

        prevPageBtn = new Button(312, 238, 20, 20, new StringTextComponent("<"), this::prevPage);
        nextPageBtn = new Button(402, 238, 20, 20, new StringTextComponent(">"), this::nextPage);

        logs = tile.getLogs();
        this.inv = tile.getInv();
    }

    @Override
    protected void init() {
        super.init();

        DevBlockTile tile = container.getTile();
        energyTextField = new TextFieldWidget(font, 63, 50, 100, 20, new StringTextComponent("0"));
        energyTextField.setResponder(this::changeEnergy);
        energyTextField.setText(String.valueOf(tile.getEnergyPerTick()));

        liquidTextField = new TextFieldWidget(font, 63, 74, 100, 20, new StringTextComponent("0"));
        liquidTextField.setResponder(this::changeLiquidAmount);
        liquidTextField.setText(String.valueOf(tile.getLiquidPerTick()));

        liquidIdTextField = new TextFieldWidget(font, 238, 74, 70, 20, new StringTextComponent("empty"));
        liquidIdTextField.setText(String.valueOf(tile.getLiquid().getRegistryName()));

        gasTextField = new TextFieldWidget(font, 63, 98, 100, 20, new StringTextComponent("0"));
        gasTextField.setResponder(this::changeGasAmount);
        gasTextField.setText(String.valueOf(tile.getGasPerTick()));

        gasIdTextField = new TextFieldWidget(font, 238, 98, 70, 20, new StringTextComponent("empty"));
        gasIdTextField.setText(String.valueOf(tile.getGas().getRegistryName()));

        itemTextField = new TextFieldWidget(font, 40, 122, 50, 20, new StringTextComponent("0"));
        itemIdTextField = new TextFieldWidget(font, 94, 122, 100, 20, new StringTextComponent("empty"));

        addButton(increaseEnergyBtn);
        addButton(energyTextField);
        addButton(decreaseEnergyBtn);
        addButton(energyActiveSignalBtn);
        energySideSelection.init(this::addButton);
        
        addButton(increaseLiquidBtn);
        addButton(liquidTextField);
        addButton(decreaseLiquidBtn);
        addButton(liquidActiveSignalBtn);
        addButton(liquidIdTextField);
        addButton(setLiquidBtn);
        liquidSideSelection.init(this::addButton);

        addButton(increaseGasBtn);
        addButton(gasTextField);
        addButton(decreaseGasBtn);
        addButton(gasActiveSignalBtn);
        addButton(gasIdTextField);
        addButton(setGasBtn);
        gasSideSelection.init(this::addButton);

        addButton(itemTextField);
        addButton(itemIdTextField);
        addButton(itemActiveSignalBtn);
        addButton(setItemBtn);
        itemSideSelection.init(this::addButton);

        addButton(prevPageBtn);
        addButton(nextPageBtn);

        guiLeft = 0;
        guiTop = 0;
    }

    private void syncEnergy() {
        container.syncEnergy(energy, energyActiveSignal, energySideSelection.getSides());
    }

    private void syncGas() {
        container.syncGas(gasAmount, gas, gasActiveSignal, gasSideSelection.getSides());
    }

    private void syncLiquid() {
        container.syncLiquid(liquidAmount, liquid, liquidActiveSignal, liquidSideSelection.getSides());
    }

    private void syncItems() {
        container.syncInv(inv, itemActiveSignal, itemSideSelection.getSides());
    }

    private void nextPage(Button button) {
        int maxPages = (int) Math.ceil(logs.size() / 10.0) - 1;

        if (page < maxPages) {
            page++;
        }
    }

    private void prevPage(Button button) {
        if (page > 0) {
            page--;
        }
    }

    private int getAmount() {
        if (Screen.hasShiftDown()) {
            return 10;
        } else if (Screen.hasControlDown()) {
            return 50;
        } else if (Screen.hasAltDown()) {
            return 100;
        } else {
            return 1;
        }
    }

    private void changeEnergy(String text) {
        try {
            int energy = Math.max(0, Integer.parseInt(text));

            if (energy != this.energy) {
                this.energy = energy;
                syncEnergy();
            }
        } catch (NumberFormatException ignored) {}
    }

    private void increaseEnergy(Button button) {
        energy = Math.max(0, energy + getAmount());
        energyTextField.setText(String.valueOf(energy));
        syncEnergy();
    }

    private void decreaseEnergy(Button button) {
        energy = Math.max(0, energy - getAmount());
        energyTextField.setText(String.valueOf(energy));
        syncEnergy();
    }

    private void changeEnergyActiveSignal(Button button) {
        energyActiveSignal = energyActiveSignal.next();
        button.setMessage(new StringTextComponent(energyActiveSignal.name()));
        syncEnergy();
    }

    private void setLiquid(Button button) {
        String text = liquidIdTextField.getText();
        if (!text.isEmpty()) {
            ResourceLocation liquidId = new ResourceLocation(text.trim());
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(liquidId);

            if (fluid != null) {
                if (!fluid.getAttributes().isGaseous() /*|| fluid == Fluids.EMPTY*/) {
                    if (fluid != liquid) {
                        liquid = fluid;
                        syncLiquid();
                    }

                    Techworks.LOGGER.debug("{} found", liquid);
                } else {
                    Techworks.LOGGER.debug("{} found, but it's gas", fluid);
                }
            } else {
                Techworks.LOGGER.debug("{} not found", liquidId);
            }
        }
    }

    private void increaseLiquid(Button button) {
        liquidAmount = Math.max(0, liquidAmount + getAmount());
        liquidTextField.setText(String.valueOf(liquidAmount));
        syncLiquid();
    }

    private void decreaseLiquid(Button button) {
        liquidAmount = Math.max(0, liquidAmount - getAmount());
        liquidTextField.setText(String.valueOf(liquidAmount));
        syncLiquid();
    }

    private void changeLiquidAmount(String text) {
        try {
            int amount = Math.max(0, Integer.parseInt(text));

            if (amount != liquidAmount) {
               liquidAmount = amount;
               syncLiquid();
            }
        } catch (NumberFormatException ignored) {}
    }

    private void changeLiquidActiveSignal(Button button) {
        liquidActiveSignal = liquidActiveSignal.next();
        button.setMessage(new StringTextComponent(liquidActiveSignal.name()));
        syncLiquid();
    }

    private void setGas(Button button) {
        String text = gasIdTextField.getText();
        if (!text.isEmpty()) {
            ResourceLocation gasId = new ResourceLocation(text.trim());
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(gasId);

            if (fluid != null) {
                if (fluid.getAttributes().isGaseous() /*|| fluid == Fluids.EMPTY*/) {
                    if (fluid != gas) {
                        gas = fluid;
                        syncGas();
                    }

                    Techworks.LOGGER.debug("{} found", gas);
                } else {
                    Techworks.LOGGER.debug("{} found, but it's liquid", fluid);
                }
            } else {
                Techworks.LOGGER.debug("{} not found", gasId);
            }
        }
    }

    private void increaseGas(Button button) {
        gasAmount = Math.max(0, gasAmount + getAmount());
        gasTextField.setText(String.valueOf(gasAmount));
        syncGas();
    }

    private void decreaseGas(Button button) {
        gasAmount = Math.max(0, gasAmount - getAmount());
        gasTextField.setText(String.valueOf(gasAmount));
        syncGas();
    }

    private void changeGasAmount(String text) {
        try {
            int amount = Math.max(0, Integer.parseInt(text));
            if (gasAmount != amount) {
                gasAmount = amount;
                syncGas();
            }
        } catch (NumberFormatException ignored) {}
    }

    private void changeGasActiveSignal(Button button) {
        gasActiveSignal = gasActiveSignal.next();
        button.setMessage(new StringTextComponent(gasActiveSignal.name()));
        syncGas();
    }

    private void setItem(Button button) {
        String itemText = itemIdTextField.getText();
        String countText = itemTextField.getText();

        if (!itemText.isEmpty() && !countText.isEmpty()) {
            ResourceLocation itemId = new ResourceLocation(itemText.trim());
            Item item = ForgeRegistries.ITEMS.getValue(itemId);

            if (item != null) {
                Techworks.LOGGER.debug("{} found", item);
            } else {
                Techworks.LOGGER.warn("{} not found", itemId);
                return;
            }


            try {
                int count = Integer.parseInt(countText);

                if (count > 0) {
//                    Techworks.LOGGER.debug("item = {}, count = {}", item, count);
                    inv.add(new ItemStack(item, count));
                    syncItems();
                } else {
                    Techworks.LOGGER.warn("Item count must be more than 0");
                }
            } catch (NumberFormatException ignore) {
            }
        }
    }

    private void changeItemActiveSignal(Button button) {
        itemActiveSignal = itemActiveSignal.next();
        button.setMessage(new StringTextComponent(itemActiveSignal.name()));
        syncItems();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int x, int y) {
        energySideSelection.renderSideLabels(stack, font);
        liquidSideSelection.renderSideLabels(stack, font);
        gasSideSelection.renderSideLabels(stack, font);
        itemSideSelection.renderSideLabels(stack, font);
        drawInventory(stack);
        drawLog(stack);
        drawFluid(stack, 21, 76, liquid);
        drawFluid(stack, 21, 100, gas);
    }

    private void drawLog(MatrixStack stack) {
//        fill(stack, 312, 145, 422, 235, 0xFF000000);
        logs = container.getTile().getLogs();
        int size = logs.size();
        int maxPages = (int) Math.ceil(logs.size() / 10.0);

        String pageText = maxPages > 0 ? (page + 1) + "/" + maxPages : "0";
        Utils.drawString(stack, font, pageText,325 + ((81 - font.getStringWidth(pageText)) / 2.0f), 244, 0xFFFFFFFF, false);

        for (int i = 9 * page; i < 9 * page + 10; i++) {
            if (i >= size) {
                break;
            }

            Utils.drawString(stack, font, logs.get(i), 312, 145 + (9 * (i - (page * 9))), 0xFFFFFFFF, false);
        }
    }

    private void drawInventory(MatrixStack stack) {
        fill(stack, 40, 145, 310, 217, 0x7D000000);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 15; j++) {
                if ((j + i * 15) < inv.size()) {
                    ItemStack itemStack = inv.get(j + i * 15);
                    itemRenderer.renderItemAndEffectIntoGUI(itemStack, 40 + (18 * j), 145 + (18 * i));
                    itemRenderer.renderItemOverlayIntoGUI(font, itemStack, 40 + (18 * j), 145 + (18 * i), null);
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int itemRow = 0;
        for (int i = 0; i < inv.size(); i++) {
            int itemCol = i % 15;

            if (i > 0 && i % 15 == 0) {
                itemRow++;
            }

            if (mouseX >= 40 + (18 * itemCol) && mouseX <= 50 + (18 * itemCol) && mouseY >= 145 + (18 * itemRow) && mouseY <= 155 + (18 * itemRow)) {
                inv.remove(i);
                syncItems();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void drawFluid(MatrixStack stack, int x, int y, Fluid fluid) {
        if (fluid != null) {
            minecraft.textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
            TextureAtlasSprite fluidTex = minecraft.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluid.getAttributes().getStillTexture());
            blit(stack, x, y, 0, 16, 16, fluidTex);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        InputMappings.Input input = InputMappings.getInputByCode(keyCode, scanCode);

        if (keyCode != 256 && minecraft.gameSettings.keyBindInventory.isActiveAndMatches(input) && (
                energyTextField.isFocused()
                || liquidTextField.isFocused()
                || liquidIdTextField.isFocused()
                || gasTextField.isFocused()
                || gasIdTextField.isFocused()
                || itemIdTextField.isFocused()
                || itemTextField.isFocused()
        )) {
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static class SideSelection {
        private final CheckboxButton downSelection;
        private final CheckboxButton topSelection;
        private final CheckboxButton frontSelection;
        private final CheckboxButton rightSelection;
        private final CheckboxButton backSelection;
        private final CheckboxButton leftSelection;
        private final int x;
        private final int y;


        private SideSelection(int x, int y, EnumSet<Side> sides, BiConsumer<CheckboxButton, Side> callback) {
            downSelection = new CallbackCheckbox(x, y, sides.contains(Side.BOTTOM), checkbox -> callback.accept(checkbox, Side.BOTTOM));
            topSelection = new CallbackCheckbox(x + 21, y, sides.contains(Side.TOP), checkbox -> callback.accept(checkbox, Side.TOP));
            frontSelection = new CallbackCheckbox(x + 42, y, sides.contains(Side.FRONT), checkbox -> callback.accept(checkbox, Side.FRONT));
            rightSelection = new CallbackCheckbox(x + 63, y, sides.contains(Side.RIGHT), checkbox -> callback.accept(checkbox, Side.RIGHT));
            backSelection = new CallbackCheckbox(x + 84, y, sides.contains(Side.BACK), checkbox -> callback.accept(checkbox, Side.BACK));
            leftSelection = new CallbackCheckbox(x + 105, y, sides.contains(Side.LEFT), checkbox -> callback.accept(checkbox, Side.LEFT));

            this.x = x;
            this.y = y;
        }

        public EnumSet<Side> getSides() {
            EnumSet<Side> sides = EnumSet.noneOf(Side.class);

            if (downSelection.isChecked()) sides.add(Side.BOTTOM);
            if (topSelection.isChecked()) sides.add(Side.TOP);
            if (frontSelection.isChecked()) sides.add(Side.FRONT);
            if (rightSelection.isChecked()) sides.add(Side.RIGHT);
            if (backSelection.isChecked()) sides.add(Side.BACK);
            if (leftSelection.isChecked()) sides.add(Side.LEFT);

            return sides;
        }

        public void renderSideLabels(MatrixStack stack, FontRenderer font) {
            Utils.drawString(stack, font, "D", x + 8, y + 2, 0xFFFFFFFF, false);
            Utils.drawString(stack, font, "T", x + 28, y + 2, 0xFFFFFFFF, false);
            Utils.drawString(stack, font, "F", x + 48, y + 2, 0xFFFFFFFF, false);
            Utils.drawString(stack, font, "R", x + 68, y + 2, 0xFFFFFFFF, false);
            Utils.drawString(stack, font, "B", x + 88, y + 2, 0xFFFFFFFF, false);
            Utils.drawString(stack, font, "L", x + 108, y + 2, 0xFFFFFFFF, false);
        }

        public void init(Function<CheckboxButton, CheckboxButton> addButton) {
            addButton.apply(downSelection);
            addButton.apply(topSelection);
            addButton.apply(frontSelection);
            addButton.apply(rightSelection);
            addButton.apply(backSelection);
            addButton.apply(leftSelection);
        }

        private static class CallbackCheckbox extends CheckboxButton {
            private final Consumer<CheckboxButton> callback;

            public CallbackCheckbox(int x, int y, boolean selected, Consumer<CheckboxButton> callback) {
                super(x, y, 20, 20, StringTextComponent.EMPTY, selected, false);
                this.callback = callback;
            }

            @Override
            public void onPress() {
                super.onPress();
                callback.accept(this);
            }
        }
    }
}
