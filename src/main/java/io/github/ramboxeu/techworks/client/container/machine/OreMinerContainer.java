package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.widget.display.EnergyDisplayWidget;
import io.github.ramboxeu.techworks.client.screen.widget.inventory.ToggleableSlotGroupWidget;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.OreMinerTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class OreMinerContainer extends BaseMachineContainer<OreMinerTile> {
    private final ToggleableSlotGroupWidget outputSlots;
    private int waitTime = -1;
    private int blocksMined;
    private int blocksToMine;

    public OreMinerContainer(int id, PlayerInventory playerInv, OreMinerTile tile) {
        super(TechworksContainers.ORE_MINER.get(), id, playerInv, tile);

        addWidget(new EnergyDisplayWidget(this, 7, 15, tile.getBatteryData()));
        outputSlots = addWidget(new ToggleableSlotGroupWidget(this, 97, 17, 3, 4, tile.getInvData(), (handler, i, x, y) -> new SlotItemHandler(handler, i, x, y) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return false;
            }
        }));

        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return tile.getWaitTime();
            }

            @Override
            public void set(int value) {
                waitTime = value;
            }
        });

        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return tile.getBlocksMined();
            }

            @Override
            public void set(int value) {
                blocksMined = value;
            }
        });

        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return tile.getBlocksToMine();
            }

            @Override
            public void set(int value) {
                blocksToMine = value;
            }
        });
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    public ToggleableSlotGroupWidget getOutputSlots() {
        return outputSlots;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getBlocksMined() {
        return blocksMined;
    }

    public int getBlocksToMine() {
        return blocksToMine;
    }
}
