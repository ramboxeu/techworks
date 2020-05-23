package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.capability.InventoryItemStackHandler;
import io.github.ramboxeu.techworks.common.capability.extensions.IInventoryItemStackHandler;
import io.github.ramboxeu.techworks.common.registration.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class ElectricGrinderTile extends AbstractMachineTile {
    public ElectricGrinderTile() {
        super(Registration.ELECTRIC_GRINDER_TILE.get(), 1);
    }

    @Override
    void run() {
        this.inventory.ifPresent(handler -> {
            handler.setStackInSlot(0, new ItemStack(Items.COBBLESTONE, 1));

            this.world.getRecipeManager().getRecipe(Registration.GRINDING_RECIPE, handler, this.world).ifPresent(recipe -> {
                Techworks.LOGGER.debug(recipe.getCraftingResult(handler));
            });
        });
    }

    @Override
    public boolean hasItemHandler() {
        return true;
    }

    @Override
    protected IInventoryItemStackHandler createItemHandler() {
        return new InventoryItemStackHandler(1);
    }

    @Override
    boolean canWork() {
        return true;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.techworks.electric_grinder");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return null;
    }
}
