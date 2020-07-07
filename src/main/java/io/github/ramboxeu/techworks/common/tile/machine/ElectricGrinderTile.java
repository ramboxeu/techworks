package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.client.container.ElectricGrinderContainer;
import io.github.ramboxeu.techworks.common.capability.InventoryItemStackHandler;
import io.github.ramboxeu.techworks.common.capability.extensions.IInventoryItemStackHandler;
import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.PredicateUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElectricGrinderTile extends BaseMachineTile {
    public ElectricGrinderTile() {
        super(Registration.ELECTRIC_GRINDER_TILE.get());
    }

    @Override
    public void tick() {

    }

    //    @Override
//    void run() {
//        this.inventory.ifPresent(handler -> {
//            this.world.getRecipeManager().getRecipe(Registration.GRINDING_RECIPE, handler, this.world).ifPresent(recipe -> {
//                this.energyStorage.ifPresent(energy -> {
//                    handler.extractItem(0, 1, false);
//                    energy.extractEnergy(50, false);
//                    handler.insertItem(2, recipe.getCraftingResult(handler), false);
//                });
//            });
//        });
//    }


//    @Override
//    protected IInventoryItemStackHandler createItemHandler() {
//        return new InventoryItemStackHandler(3) {
//            @Override
//            protected void onContentsChanged(int slot) {
//                markDirty();
//            }
//
//            @Override
//            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
//                switch (slot) {
//                    case 1: return PredicateUtils.isEnergyStorage(stack);
//                    case 0:
//                    case 2:
//                        return true;
//                    default: return false;
//                }
//            }
//
//            @Override
//            public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
//                super.setStackInSlot(slot, stack);
//            }
//
//            @Override
//            public int getSlotLimit(int slot) {
//                return slot == 0 ? 1 : super.getSlotLimit(slot);
//            }
//        };
//    }

//    @Override
//    protected IEnergyStorage createEnergyStorage() {
//        return new EnergyStorage(5000, 100);
//    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.techworks.electric_grinder");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity entity) {
        return new ElectricGrinderContainer(id, inventory, this);
    }
}
