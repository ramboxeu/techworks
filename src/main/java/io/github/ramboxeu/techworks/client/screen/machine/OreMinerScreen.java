package io.github.ramboxeu.techworks.client.screen.machine;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.OreMinerContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.client.screen.OreFilterConfigScreen;
import io.github.ramboxeu.techworks.client.util.ClientUtils;
import io.github.ramboxeu.techworks.common.lang.TranslationKey;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.tile.OreMinerTile;
import net.minecraft.block.Block;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.*;

import java.util.Arrays;
import java.util.List;

public class OreMinerScreen extends BaseMachineScreen<OreMinerTile, OreMinerContainer> {
    public static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/ore_miner.png");
    public static final ResourceLocation FRONT = new ResourceLocation(Techworks.MOD_ID, "block/ore_miner_front_off");
    private static final ResourceLocation WRENCH = new ResourceLocation(Techworks.MOD_ID, "textures/item/wrench.png");

    private static final Style STYLE = Style.EMPTY.applyFormatting(TextFormatting.DARK_GRAY);

    private boolean cycleFilterEntries;
    private ItemStack filterEntry = ItemStack.EMPTY;
    private int entryIndex = 0;
    private int entryCyclingTimer = 0;

    public OreMinerScreen(OreMinerContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, TEX, FRONT);
        onFilterChanged();
    }

    @Override
    public void tick() {
        super.tick();

        if (cycleFilterEntries) {
            if (entryCyclingTimer != 50)
                entryCyclingTimer++;

            if (entryCyclingTimer == 50) {
                entryCyclingTimer = 0;
                updateFilterEntry();
            }
        }
    }

    private void updateFilterEntry() {
        OreMinerTile.IOreFilter filter = container.getMachineTile().getFilter();

        if (filter instanceof OreMinerTile.TagOreFilter) {
            ITag<Block> tag = ((OreMinerTile.TagOreFilter) filter).getTag();
            List<Block> elements = tag.getAllElements();
            filterEntry = new ItemStack(elements.get(entryIndex));
            entryIndex++;

            if (entryIndex >= elements.size()) entryIndex = 0;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int x, int y) {
        super.drawGuiContainerForegroundLayer(stack, x, y);

        OreMinerTile tile = container.getMachineTile();

        stack.push();
        stack.translate(29, 18, 0);
        stack.scale(0.5f, 0.5f, 0);
        ClientUtils.drawString(stack, font, getStatusText(tile), 5, 5, false);
        ClientUtils.drawString(stack, font, getBlocksText(TranslationKeys.BLOCKS_TO_MINE, container.getBlocksToMine()), 5, 19, false);
        ClientUtils.drawString(stack, font, getBlocksText(TranslationKeys.BLOCKS_MINED, container.getBlocksMined()), 5, 33, false);
        ClientUtils.drawString(stack, font, getAreaText(tile), 5, 47, false);
        ClientUtils.drawString(stack, font, TranslationKeys.RESOURCE.styledText(STYLE), 5, 68, false);

        if (tile.isFilterSet()) ClientUtils.drawString(stack, font, tile.getFilter().text().getStringTruncated(15), 5, 82, 0x555555, false);
        else ClientUtils.drawString(stack, font, TranslationKeys.NOT_SET.styledText(STYLE), 5, 82, false);

        stack.pop();

        if (container.getWaitTime() > -1) {
            stack.push();
            stack.translate(-22, 7, 0);

            minecraft.textureManager.bindTexture(background);
            blit(stack, 0, 0, 176, 22, 22, 22);

            stack.pop();
        }

        if (filterEntry.isEmpty()) {
            minecraft.textureManager.bindTexture(WRENCH);
            blit(stack, 77, 54, 0, 0, 0, 12, 12, 16, 16);
        } else {
            itemRenderer.renderItemIntoGUI(filterEntry, 75, 52);
        }
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack stack, int mouseX, int mouseY) {
        int x = mouseX - guiLeft;
        int y = mouseY - guiTop;

        if (x >= 31 && y >= 59 && x <= 72 && y <= 64) {
            OreMinerTile tile = container.getMachineTile();

            if (tile.isFilterSet())
                func_243308_b(stack, Arrays.asList(TranslationKeys.RESOURCE.text(), tile.getFilter().text()), mouseX, mouseY);
        } else if (x >= 75 && y >= 52 && x <= 91 && y <= 68) {
            func_243308_b(stack, Arrays.asList(TranslationKeys.PUT_STACK_TO_SET_FILTER.text(), TranslationKeys.LEFT_CLICK_TO_CONFIGURE.text(), TranslationKeys.RIGHT_CLICK_TO_CLEAR.text()), mouseX, mouseY);
        } else if (x >= -22 && y >= 7 && x <= 0 && y <= 29) {
            int waitTime = container.getWaitTime();

            if (waitTime > -1) {
                func_243308_b(stack, Arrays.asList(TranslationKeys.NO_MATCHING_BLOCKS_FOUND.text(), TranslationKeys.WAITING_TICKS.text(600 - waitTime), TranslationKeys.SCAN_NOW.text()), mouseX, mouseY);
            }
        } else {
            super.renderHoveredTooltip(stack, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (int) mouseX - guiLeft;
        int y = (int) mouseY - guiTop;

        if (x >= 75 && y >= 52 && x <= 91 && y <= 68) {
            if (button == ClientUtils.LMB) {
                ItemStack mouseStack = playerInventory.getItemStack();

                if (!mouseStack.isEmpty()) {
                    if (!container.getMachineTile().isFilterSet()) {
                        setFilterItem(mouseStack);
                    }
                } else {
                    container.getOutputSlots().setState(false, true);
                    minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    ClientUtils.changeCurrentScreen(this, new OreFilterConfigScreen(this), false);
                }

                return true;
            } else if (button == ClientUtils.RMB) {
                clearFilter();
                return true;
            }
        }

        if (x >= -22 && y >= 7 && x <= 0 && y <= 29) {
            if (container.getWaitTime() > -1) {
                TechworksPacketHandler.minerRescan(container.getMachineTile().getPos());
                minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void init() {
        super.init();
        container.getOutputSlots().setState(true, true);
    }

    public void onFilterChanged() {
        if (container.getMachineTile().isFilterSet()) {
            OreMinerTile.IOreFilter filter = container.getMachineTile().getFilter();
            OreMinerTile.FilterType type = filter.type();

            if (type == OreMinerTile.FilterType.TAG) {
                updateFilterEntry();
                cycleFilterEntries = true;
            } else {
                filterEntry = new ItemStack(((OreMinerTile.BlockOreFilter)filter).getBlock());
                cycleFilterEntries = false;
            }

            entryCyclingTimer = 0;
            entryIndex = 0;
        } else {
            filterEntry = ItemStack.EMPTY;
            cycleFilterEntries = false;
        }
    }

    private static ITextComponent getAreaText(OreMinerTile tile) {
        return TranslationKeys.AREA.styledText(STYLE).appendString(": 3x3 ").appendSibling(TranslationKeys.CHUNKS.text());
    }

    private static ITextComponent getStatusText(OreMinerTile tile) {
        IFormattableTextComponent text = TranslationKeys.STATUS.styledText(STYLE).appendString(": ");

        switch (tile.getStatus()) {
            case IDLE:
                return text.appendSibling(TranslationKeys.STATUS_IDLE.styledText(STYLE));
            case MINING:
                return text.appendSibling(TranslationKeys.STATUS_MINING.styledText(STYLE));
            case SCANNING:
                return text.appendSibling(TranslationKeys.STATUS_SCANNING.styledText(STYLE));
        }

        throw new AssertionError();
    }

    private static ITextComponent getBlocksText(TranslationKey key, int blocks) {
        TranslationTextComponent text = key.styledText(STYLE);

        if (blocks >= 0) return text.appendString(": " + blocks);
        else return text.appendString(": ???");
    }

    public void clearFilter() {
        container.getMachineTile().clearFilter(true);
        onFilterChanged();
    }

    public boolean setFilterTag(String text) {
        if (container.getMachineTile().updateFilterTag(text)) {
            onFilterChanged();
            return true;
        }

        return false;
    }

    public void setFilterItem(ItemStack stack) {
        container.getMachineTile().updateFilterItem(stack);
        onFilterChanged();
    }

    public String getFilterTagName() {
        OreMinerTile.IOreFilter filter = container.getMachineTile().getFilter();

        if (filter instanceof OreMinerTile.TagOreFilter)
            return ((OreMinerTile.TagOreFilter) filter).getName().toString();

        return "";
    }

    public ItemStack getFilterEntry() {
        return filterEntry;
    }

    public PlayerInventory getPlayerInv() {
        return playerInventory;
    }
}
