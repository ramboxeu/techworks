package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.AssemblyTableContainer;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tag.TechworksItemTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AssemblyTableTile extends TileEntity implements INamedContainerProvider {
    // 0 blueprint, 1 casings, 2-5 tools, 6-14 crafting grid
    private final IItemHandler inventory;
    private final IItemHandler outputInv;

    public AssemblyTableTile() {
        super(TechworksTiles.ASSEMBLY_TABLE.getTileType());

        inventory = new ItemStackHandler(15) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
                craftingInventoryChanged(slot);
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return slot != 0 || stack.getItem().isIn(TechworksItemTags.BLUEPRINTS);
            }
        };

        outputInv = new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return false;
            }

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }
        };
    }

    private void craftingInventoryChanged(int slot) {
        if (!world.isRemote) {
            Techworks.LOGGER.debug("Crafting inv changed! Slot: {}", slot);
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.techworks.assembly_table");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new AssemblyTableContainer(id, inventory, this);
    }

    public IItemHandler getInventory() {
        return inventory;
    }

    public IItemHandler getOutputInv() {
        return outputInv;
    }
}
