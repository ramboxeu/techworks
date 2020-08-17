package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksBlocks;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tag.TechworksItemTags;
import io.github.ramboxeu.techworks.common.tile.AssemblyTableTile;
import io.github.ramboxeu.techworks.common.util.inventory.SlotBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class AssemblyTableContainer extends Container {
    private final IItemHandler inv;
    private final IItemHandler outputInv;

    public AssemblyTableContainer(int id, PlayerInventory inventory, AssemblyTableTile tile) {
        super(TechworksContainers.ASSEMBLY_TABLE.getContainer(), id);

        InvWrapper playerInventory = new InvWrapper(inventory);
        inv = tile.getInventory();
        outputInv = tile.getOutputInv();

        // Blueprint
        addSlot(new SlotBuilder(inv, 0).pos(44, 28).predicate(stack -> stack.getItem().isIn(TechworksItemTags.BLUEPRINTS)).build());

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
        addSlot(new SlotBuilder(outputInv, 0).output(true).pos(137, 46).build());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new SlotItemHandler(playerInventory, j + i * 9 + 9, 8 + j * 18, 104 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new SlotItemHandler(playerInventory, i, 8 + i *18, 162));
        }
    }

    private void onCraftingSlotChanged(ItemStack oldStack, ItemStack newStack) {
        if (!oldStack.isItemEqual(newStack) && oldStack.getCount() != newStack.getCount()) {
            Techworks.LOGGER.debug("Crafting matrix changed!");
            outputInv.insertItem(0, new ItemStack(TechworksBlocks.BOILER.getItem(), 64), false);
        }
    }

    private void onOutputSlotChanged(ItemStack oldStack, ItemStack newStack) {
        if (!oldStack.isItemEqual(newStack)) {
            if (oldStack.getCount() > newStack.getCount()) {
                Techworks.LOGGER.debug("Consuming items!");
            }
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
