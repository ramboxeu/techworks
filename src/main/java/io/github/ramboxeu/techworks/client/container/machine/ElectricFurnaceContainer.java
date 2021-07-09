package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.widget.config.ComponentsWidget;
import io.github.ramboxeu.techworks.client.screen.widget.display.EnergyDisplayWidget;
import io.github.ramboxeu.techworks.client.screen.widget.inventory.SlotWidget;
import io.github.ramboxeu.techworks.client.screen.widget.progress.ArrowProgressWidget;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.machine.ElectricFurnaceTile;
import io.github.ramboxeu.techworks.common.util.Predicates;
import io.github.ramboxeu.techworks.common.util.Side;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ElectricFurnaceContainer extends BaseMachineContainer<ElectricFurnaceTile> {
    private int workTime;
    private int cookTime;

    private final ComponentsWidget componentsWidget;

    public ElectricFurnaceContainer(int id, PlayerInventory inv, ElectricFurnaceTile tile, Map<Side, List<HandlerConfig>> dataMap) {
        this(id, inv, tile, dataMap, IWorldPosCallable.DUMMY);
    }

    public ElectricFurnaceContainer(int id, PlayerInventory playerInventory, ElectricFurnaceTile tile, Map<Side, List<HandlerConfig>> dataMap, IWorldPosCallable callable) {
        super(TechworksContainers.ELECTRIC_FURNACE.get(), id, playerInventory, tile, dataMap);

//        addWidget(new EnergyDisplayWidget(this, 8, 14, tile.getBatteryData()));
        addWidget(new SlotWidget(this, 55, 34, 0, false, tile.getInvData()));

        // FIXME: not really dropping xp
        addWidget(new SlotWidget(this, 111, 30, 0,  true, tile.getOutputInvData(),
                (handler, index, x, y) -> new SlotItemHandler(handler, index, x, y) {
                    @Override
                    public boolean isItemValid(@NotNull ItemStack stack) {
                        return false;
                    }

                    @Override
                    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
                        callable.consume((world, pos) -> {
                            if (!world.isRemote) {
                                float xp = tile.resetXP();

                                double x = pos.getX();
                                double y = pos.getY();
                                double z = pos.getZ();

                                while (xp > 0) {
                                    int orbXP = ExperienceOrbEntity.getXPSplit((int) xp);
                                    xp -= orbXP;
                                    world.addEntity(new ExperienceOrbEntity(world, x, y, z, orbXP));
                                }
                            }
                        });

                        return super.onTake(player, stack);
                    }
                })
        );

        addWidget(new ArrowProgressWidget(81, 36, false, tile::getNeededEnergy, tile::getExtractedEnergy));
        addWidget(new EnergyDisplayWidget(this, 8, 14, 18, 56, tile.getBatteryData()));
        componentsWidget = addWidget(new ComponentsWidget(tile.getComponentStorage()));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack slotItemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null) {
            slotItemStack = slot.getStack();
            itemStack = slotItemStack.copy();
            if (index < 36) {
                if (Predicates.isEnergyStorage(itemStack)) {
                    Slot energyOutput = this.inventorySlots.get(37);

                    ItemStack itemStack1 = ItemStack.EMPTY;

                    itemStack1 = itemStack.split(1);

                    if (!energyOutput.getHasStack() && energyOutput.isItemValid(itemStack)) {
                        energyOutput.putStack(itemStack1);
                        slot.putStack(itemStack);
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                if (!this.mergeItemStack(itemStack, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChanged();
            }
        }

        slot.putStack(itemStack);

        return ItemStack.EMPTY;
    }

    public int getWorkTime() {
        return workTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public ComponentsWidget getComponentsWidget() {
        return componentsWidget;
    }
}
