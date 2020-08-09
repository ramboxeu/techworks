package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.registration.TechworksItems;
import io.github.ramboxeu.techworks.common.tile.BlueprintTableTile;
import io.github.ramboxeu.techworks.common.util.inventory.SlotBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;

public class BlueprintTableContainer extends Container {
    private final ItemStackHandler inventory;
    private final ItemStackHandler outputInv;
    private final IWorldPosCallable callable;

    public BlueprintTableContainer(int id, PlayerInventory inv, BlueprintTableTile tile) {
        this(id, inv, tile, IWorldPosCallable.DUMMY);
    }

    public BlueprintTableContainer(int id, PlayerInventory inv, BlueprintTableTile tile, IWorldPosCallable callable) {
        super(TechworksContainers.BLUEPRINT_TABLE.getContainer(), id);

        this.callable = callable;

        inventory = tile.getInventory();

        outputInv = new ItemStackHandler() {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return false;
            }
        };

        addSlot(new SlotBuilder(inventory, 0).pos(9, 46).predicate(stack -> stack.getItem().isIn(Tags.Items.DYES_WHITE)).build());
        addSlot(new SlotBuilder(inventory, 1).pos(39, 46).predicate(stack -> stack.getItem() == TechworksItems.EMPTY_BLUEPRINT.getItem()).build());
        addSlot(new SlotBuilder(outputInv, 0).pos(24, 92).output(true).build());

        InvWrapper playerInv = new InvWrapper(inv);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new SlotItemHandler(playerInv, j + i * 9 + 9, 8 + j * 18, 134 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new SlotItemHandler(playerInv, i, 8 + i * 18, 192));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);

        callable.consume(((world, blockPos) -> this.clearContainer(playerIn, world, new RecipeWrapper(outputInv))));
    }
}
