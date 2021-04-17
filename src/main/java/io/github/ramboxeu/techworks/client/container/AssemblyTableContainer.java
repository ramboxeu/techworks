package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.AssemblyTableTile;
import io.github.ramboxeu.techworks.common.util.inventory.SlotBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

public class AssemblyTableContainer extends BaseContainer implements AssemblyTableTile.IListener {
    private final AssemblyTableTile tile;
    private final PlayerEntity player;
    private final Slot outputSlot;

    public AssemblyTableContainer(int id, PlayerInventory inventory, AssemblyTableTile tile) {
        super(TechworksContainers.ASSEMBLY_TABLE.get(), id);

        InvWrapper playerInventory = new InvWrapper(inventory);
        IItemHandler inv = tile.getInventory();
        IItemHandler outputInv = tile.getOutputInv();

        this.tile = tile;

        // Blueprint
        addSlot(new SlotItemHandler(inv, 0, 44, 28));

        // Casings
        addSlot(new SlotBuilder(inv, 1).pos(44, 64).build());

        // Tools
        addSlot(new SlotBuilder(inv, 2).pos(8, 18).build());
        addSlot(new SlotBuilder(inv, 3).pos(8, 36).build());
        addSlot(new SlotBuilder(inv, 4).pos(8, 54).build());
        addSlot(new SlotBuilder(inv, 5).pos(8, 72).build());

        // Crafting grid
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlot(new SlotItemHandler(inv, (j + i * 3) + 6, 65 + j * 18, 28 + i * 18));
            }
        }

        // Output slot
        outputSlot = addSlot(new SlotItemHandler(outputInv, 0, 137, 46) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return false;
            }

            public void onSlotChange(ItemStack oldStack, ItemStack newStack) {
                int i = newStack.getCount() - oldStack.getCount();
                if (i > 0) {
                    tile.onCraft(i);
                }
            }

            @Override
            public ItemStack onTake(PlayerEntity Player, ItemStack stack) {
                tile.onCraft(1);

                return super.onTake(player, stack);
            }
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new SlotItemHandler(playerInventory, j + i * 9 + 9, 8 + j * 18, 104 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new SlotItemHandler(playerInventory, i, 8 + i *18, 162));
        }

        tile.addListener(this);
        player = inventory.player;
    }

    @Override
    public @Nonnull ItemStack transferStackInSlot(@Nonnull PlayerEntity player, int index) {
        Slot slot = inventorySlots.get(index);
        ItemStack returnStack = ItemStack.EMPTY;

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            returnStack = slotStack.copy();

            if (index == 15) { // Output slot
                tile.onCraftStack(player, windowId);
                return ItemStack.EMPTY;
            }
        }

        return returnStack;
    }

    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);

        tile.removeListener(this);
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public void syncOutputSlot() {
        ((ServerPlayerEntity) player).connection.sendPacket(new SSetSlotPacket(windowId, outputSlot.slotNumber, outputSlot.getStack()));
    }

    @Override
    public void syncCraftingSlots() {
        for (int i = 0; i < 15; i++) {
            Slot slot = inventorySlots.get(i);
            ((ServerPlayerEntity) player).connection.sendPacket(new SSetSlotPacket(windowId, i, slot.getStack()));
        }
    }
}
