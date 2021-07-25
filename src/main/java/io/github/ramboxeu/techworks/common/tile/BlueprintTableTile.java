package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.client.container.BlueprintTableContainer;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.registration.TechworksItems;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlueprintTableTile extends BaseTechworksTile implements INamedContainerProvider {
    // 0 white dye, 1 empty blueprint
    private final ItemStackHandler inventory;

    public BlueprintTableTile() {
        super(TechworksTiles.BLUEPRINT_TABLE.get());

        inventory = new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return slot == 0 ?
                        stack.getItem().isIn(Tags.Items.DYES_WHITE) :
                        stack.getItem() == TechworksItems.EMPTY_BLUEPRINT.get();
            }
        };
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public ITextComponent getDisplayName() {
        return TranslationKeys.BLUEPRINT_TABLE.text();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
        return new BlueprintTableContainer(id, inv, this, IWorldPosCallable.of(world, pos));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("Inventory", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null));
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, compound.get("Inventory"));
        super.read(state, compound);
    }
}
