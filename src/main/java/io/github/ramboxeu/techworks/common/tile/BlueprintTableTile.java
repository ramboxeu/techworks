package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.client.container.BlueprintTableContainer;
import io.github.ramboxeu.techworks.common.registration.TechworksItems;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlueprintTableTile extends BaseTechworksTile implements INamedContainerProvider {
    // 0 black dye, 1 empty blueprint
    private final ItemStackHandler inventory;

    public BlueprintTableTile() {
        super(TechworksTiles.BLUEPRINT_TABLE.getTileType());

        inventory = new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return slot == 0 ?
                        stack.getItem().isIn(Tags.Items.DYES_BLACK) :
                        stack.getItem() == TechworksItems.EMPTY_BLUEPRINT.getItem();
            }
        };
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.techworks.blueprint_table");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
        return new BlueprintTableContainer(id, inv, this, IWorldPosCallable.of(world, pos));
    }
}
