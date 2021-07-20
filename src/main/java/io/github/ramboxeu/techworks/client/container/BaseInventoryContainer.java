package io.github.ramboxeu.techworks.client.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public abstract class BaseInventoryContainer extends BaseContainer {
    protected IItemHandler playerInventory;
    protected int firstPlayerSlot = -1;

    public BaseInventoryContainer(@Nullable ContainerType<?> type, PlayerInventory playerInv, int id) {
        super(type, id);
        playerInventory = new InvWrapper(playerInv);
    }

    protected void layoutPlayerSlots() {
        layoutPlayerSlots(0, 0);
    }

    protected void layoutPlayerSlots(int xOffset, int yOffset) {
        firstPlayerSlot = inventorySlots.size();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new SlotItemHandler(playerInventory, j + i * 9 + 9, (8 + j * 18) + xOffset, (84 + i * 18) + yOffset));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new SlotItemHandler(playerInventory, i, (8 + i * 18) + xOffset, 142 + yOffset));
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        Slot source = inventorySlots.get(index);

        if (source != null) {
            ItemStack sourceStack = source.getStack();

            int lastPlayerSlot = firstPlayerSlot + 36;
            if (index >= firstPlayerSlot && index < lastPlayerSlot) {
                ItemStack stack = sourceStack.copy();

                for (int i = 0; i < inventorySlots.size(); i++) {
                    if (i >= firstPlayerSlot && i < lastPlayerSlot) {
                        i = lastPlayerSlot;
                    }

                    Slot target = inventorySlots.get(i);
                    ItemStack targetStack = target.getStack().copy();

                    if (!targetStack.isEmpty()) {
                        if (areItemsAndTagsEqual(stack, targetStack)) {
                            int size = target.getItemStackLimit(stack) - targetStack.getCount();

                            if (stack.getCount() <= size) {
                                targetStack.grow(stack.getCount());
                                target.putStack(targetStack);
                                stack = ItemStack.EMPTY;
                                break;
                            } else {
                                targetStack.grow(size);
                                stack.shrink(size);
                                target.putStack(targetStack);
                            }
                        }
                    } else {
                        if (target.isItemValid(stack)) {
                            int size = target.getItemStackLimit(stack);

                            if (stack.getCount() <= size) {
                                target.putStack(stack);
                                stack = ItemStack.EMPTY;
                                break;
                            } else {
                                target.putStack(stack.split(size));
                            }
                        }
                    }
                }

                source.putStack(stack);
                return ItemStack.EMPTY;
            } else {
                if (!this.mergeItemStack(sourceStack, firstPlayerSlot, lastPlayerSlot, true)) {
                    return ItemStack.EMPTY;
                }

                source.onSlotChanged();
            }
        }

        return ItemStack.EMPTY;
    }
}
