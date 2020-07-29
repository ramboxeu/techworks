package io.github.ramboxeu.techworks.common.capability.extensions;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandlerModifiable;

// Obsolete as Forge provides it's own RecipeHandler wrapper for handlers
public interface IInventoryItemStackHandler extends IInventory, IItemHandlerModifiable { }
