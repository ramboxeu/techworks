package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.api.component.ComponentStackHandler;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ComponentsContainer extends Container {
    private final ComponentStackHandler components;

    public static final int SLOT_WIDTH = 16;
    public static final int SLOT_HEIGHT = 16;
    public static final int X_OFFSET = 8;
    public static final int Y_OFFSET = 18;
    public static final int Y_SPACING = 2;

    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;

    public ComponentsContainer(int id, PlayerInventory inventory, ComponentStackHandler components) {
        super(TechworksContainers.COMPONENTS.get(), id);

        this.components = components;

        int x = components.getSlots();

        if (x > 0) {

            int rows = 1;
            int slots = x;

            if (x > 5) {
                if (x % 2 == 0) {
                    rows = 2;
                    slots = x / 2;
                } else if (x % 3 == 0) {
                    rows = 3;
                    slots = x / 3;
                } else {
                    if (x % 5 == 1) {
                        slots = (int) Math.ceil((x / 2.0F));
                    } else {
                        rows = 2;
                        slots = 5;
                    }

                }
            }

            int space = WIDTH - X_OFFSET * 2;
            int spacing = (space / slots) - SLOT_WIDTH;
            int xOffset = (space - ((spacing + SLOT_WIDTH) * slots)) + (spacing / 2) + X_OFFSET;

            int index = 0;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < slots; j++) {
                    addSlot(new SlotItemHandler(components, index,
                            ((SLOT_WIDTH + spacing) * j) + xOffset,
                            ((SLOT_HEIGHT + Y_SPACING) * i) + Y_OFFSET)
                    );

                    index++;
                }

                if (x - slots > 0 && x - slots < slots) {
                    slots = x - slots;
                    xOffset = (space - ((spacing + SLOT_WIDTH) * slots)) + (spacing / 2) + X_OFFSET;
                }

                x -= slots;
            }
        }

        // Main inventory
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Hotbar
        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public ComponentStackHandler getComponents() {
        return components;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        Slot sourceSlot = getSlot(index);
        ItemStack stack = sourceSlot.getStack();

        if (index > components.getSlots() - 1) {
            for (int i = 0; i < components.getSlots() - 1; i++) {
                Slot targetSlot = getSlot(i);
                if (components.isItemValid(i, stack) && !targetSlot.getHasStack()) {
                    targetSlot.putStack(stack);
                    sourceSlot.putStack(ItemStack.EMPTY);
                    break;
                }
            }
        } else {
            if (mergeItemStack(stack, components.getSlots(), inventorySlots.size(), false)) {
                sourceSlot.putStack(ItemStack.EMPTY);
            }
        }

        return ItemStack.EMPTY;
    }
}
