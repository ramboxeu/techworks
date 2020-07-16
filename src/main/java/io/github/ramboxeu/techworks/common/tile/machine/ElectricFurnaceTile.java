package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.api.component.ComponentStackHandler;
import io.github.ramboxeu.techworks.client.container.machine.ElectricFurnaceContainer;
import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class ElectricFurnaceTile extends BaseMachineTile {
    public ElectricFurnaceTile() {
        super(Registration.ELECTRIC_FURNACE_TILE.get(), new ComponentStackHandler.Builder(0));
    }

//    @Override
//    void run() {
//        this.inventory.ifPresent(inv -> {
//            this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, inv, this.world).ifPresent(recipe -> {
//                this.energyStorage.ifPresent(energy -> {
//                    inv.extractItem(0, 1, false);
//                    energy.extractEnergy(50, false);
//                    inv.insertItem(2, recipe.getCraftingResult(inv), false);
//                });
//            });
//        });
//    }

    @Override
    public void tick() {

    }


//    protected IEnergyStorage createEnergyStorage() {
//        return new EnergyStorage(5000, 100, 100);
//    }

//    @Override
//    protected IInventoryItemStackHandler createItemHandler() {
//        return new InventoryItemStackHandler(3) {
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
//            public int getSlotLimit(int slot) {
//                return slot == 0 ? 1 : super.getSlotLimit(slot);
//            }
//
//            @Override
//            protected void onContentsChanged(int slot) {
//                markDirty();
//            }
//        };
//    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.techworks.electric_furnace");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity entity) {
        return new ElectricFurnaceContainer(id, inventory, this);
    }
}
