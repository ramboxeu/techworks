package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.client.container.AssemblyTableContainer;
import io.github.ramboxeu.techworks.common.recipe.MachineAssemblyRecipe;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tag.TechworksItemTags;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssemblyTableTile extends BaseTechworksTile implements INamedContainerProvider {
    // 0 blueprint or machine, 1 casings, 2-5 tools, 6-14 crafting grid
    private final ItemStackHandler inventory;
    private final IInventory recipeInv;
    private final ItemStackHandler outputInv;
    private MachineAssemblyRecipe recipe;
    private final List<IListener> listeners;

    public AssemblyTableTile() {
        super(TechworksTiles.ASSEMBLY_TABLE.get());

        inventory = new ItemStackHandler(15) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
                craftingInventoryChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return slot != 0
                        || stack.getItem().isIn(TechworksItemTags.BLUEPRINTS)
                        || stack.getItem().isIn(TechworksItemTags.MACHINES);
            }
        };

        outputInv = new ItemStackHandler() {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
                craftingOutputChanged();
            }
        };

        recipeInv = new RecipeWrapper(inventory);

        listeners = new ArrayList<>();
    }

    @Override
    protected void onFirstTick() {
        craftingInventoryChanged(); // Have a result after load, so user doesn't have to take item and put it back in to
    }

    private void craftingInventoryChanged() {
        if (world != null && !world.isRemote) {
            Optional<MachineAssemblyRecipe> optional = world.getRecipeManager().getRecipe(TechworksRecipes.MACHINE_ASSEMBLY.get(), recipeInv, world);

            if (optional.isPresent()) {
                if (outputInv.extractItem(0, 1, true).isEmpty()) {
                    MachineAssemblyRecipe recipe = optional.get();
                    this.recipe = recipe;
                    ItemStack result = recipe.getCraftingResult(recipeInv);

                    if (!result.isEmpty()) {
                        if (outputInv.insertItem(0, result, true).isEmpty()) {
                            outputInv.insertItem(0, result, false);
                        }
                    }
                }
            } else {
                outputInv.setStackInSlot(0, ItemStack.EMPTY);
                recipe = null;
            }
        }
    }

    private void craftingOutputChanged() {
        if (world != null && !world.isRemote) {
            listeners.forEach(IListener::syncOutputSlot);
        }
    }

    public void onCraft(int size) {
        if (world != null && !world.isRemote && recipe != null) {
            ItemStack mainSlot = inventory.getStackInSlot(0);

            if (!mainSlot.getItem().isIn(TechworksItemTags.BLUEPRINTS)) {
                mainSlot.shrink(size);
            }

            // Second slot
            if (!mainSlot.getItem().isIn(TechworksItemTags.MACHINES)) {
                inventory.getStackInSlot(1).shrink(size);
            }

            boolean[] tools = recipe.getTools();

            for (int i = 0; i < 4; i++) {
                if (tools[i]) {
                    ItemStack stack = inventory.getStackInSlot(i + 2);
                    stack.setDamage(stack.getDamage() + size);

                    if (stack.getDamage() == stack.getMaxDamage()) {
                        inventory.setStackInSlot(i + 2, ItemStack.EMPTY);
                    }
                }
            }

            for (int i = 0; i < 9; i++) {
                inventory.getStackInSlot(i + 6).shrink(size);
            }

            listeners.forEach(IListener::syncCraftingSlots);

            craftingInventoryChanged();
        }
    }

    public void onCraftStack(PlayerEntity player, int windowId) {
        if (world != null && !world.isRemote && recipe != null) {
            PlayerInventory playerInv = player.inventory;
            ItemStack resultStack = outputInv.getStackInSlot(0);
            ItemStack mainSlot = inventory.getStackInSlot(0);
            boolean flag = mainSlot.getItem().isIn(TechworksItemTags.BLUEPRINTS);
            int slotPadding = 7; // So stack will line up in the inventory

            int min = flag ? inventory.getStackInSlot(1).getCount() : mainSlot.getCount();

            for (int i = (flag ? 1 : 0); i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack.isEmpty()) continue;

                int count = i >= 2 && i <= 5 ? (stack.getMaxDamage() - stack.getDamage()) : stack.getCount();

                if (count < min) {
                    min = count;
                }
            }

            int count = resultStack.getCount();
            int amount = min * count;
            int crafted = 0;
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

            // Inverse direction
            for (int i = playerInv.mainInventory.size() - 1; i >= 0; i--) {
                if (amount == 0) {
                    break;
                }

                ItemStack invStack = playerInv.mainInventory.get(i);

                if (resultStack.getItem() == invStack.getItem() && ItemStack.areItemStackTagsEqual(resultStack, invStack)) {
                    int space = invStack.getMaxStackSize() - invStack.getCount();

                    if (space > 0) {
                        int size = Math.min(amount, space);
                        invStack.grow(size);
                        amount -= size;
                        crafted += size;
                        serverPlayer.connection.sendPacket(new SSetSlotPacket(windowId, i + slotPadding, invStack));
                    }
                }

                if (invStack.isEmpty()) {
                    int size = Math.min(amount, resultStack.getMaxStackSize());

                    ItemStack stack = resultStack.copy();
                    stack.setCount(size);

                    playerInv.mainInventory.set(i, stack);
                    amount -= size;
                    crafted += size;
                    serverPlayer.connection.sendPacket(new SSetSlotPacket(windowId, i + slotPadding, stack));
                }
            }

            if (crafted > 0) {
                amount -= (count * (amount / count));

                if (amount > 0) {
                    ItemStack remainder = resultStack.copy();
                    remainder.setCount(amount);
                    player.dropItem(remainder, false);
                }

                onCraft((crafted + amount) / count);
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        Utils.writeInvToNbt(nbt, "Inventory", inventory);
        Utils.writeInvToNbt(nbt, "OutputInv", outputInv);

        return super.write(nbt);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        Utils.readInvFromNbt(nbt, "Inventory", inventory);
        Utils.readInvFromNbt(nbt, "OutputInv", outputInv);

        super.read(state, nbt);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.techworks.assembly_table");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new AssemblyTableContainer(id, inventory, this);
    }

    public IItemHandler getInventory() {
        return inventory;
    }

    public IItemHandler getOutputInv() {
        return outputInv;
    }

    public void addListener(IListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(IListener listener) {
        listeners.remove(listener);
    }

    // Always called server side
    public interface IListener {
        void syncOutputSlot();
        void syncCraftingSlots();
    }
}
