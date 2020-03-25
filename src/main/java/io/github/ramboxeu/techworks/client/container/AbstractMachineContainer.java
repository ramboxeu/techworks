package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.api.gas.IGasHandler;
import io.github.ramboxeu.techworks.common.tile.AbstractMachineTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public abstract class AbstractMachineContainer extends Container {
    protected IItemHandler playerInventory;
    protected LazyOptional<IItemHandler> inventory;
    protected LazyOptional<IEnergyStorage> energyStorage;
    protected LazyOptional<IGasHandler> gasHandler;

    protected AbstractMachineContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, AbstractMachineTile machineTile) {
        super(containerType, id);

        this.playerInventory = new InvWrapper(playerInventory);
        this.inventory = machineTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        this.energyStorage = machineTile.getCapability(CapabilityEnergy.ENERGY);
        // TODO: Implement gas

        this.layoutPlayerSlots();

        this.inventory.ifPresent(this::layoutSlots);
    }

    @Override
    public abstract boolean canInteractWith(PlayerEntity playerIn);

    private void layoutPlayerSlots() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new SlotItemHandler(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new SlotItemHandler(playerInventory, i, 8 + i *18, 142));
        }
    }

    protected void layoutSlots(IItemHandler handler) {}
}
