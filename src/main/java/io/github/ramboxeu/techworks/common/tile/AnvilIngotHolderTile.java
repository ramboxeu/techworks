package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.common.block.AnvilIngotHolderBlock;
import io.github.ramboxeu.techworks.common.item.handler.SingleStackStorage;
import io.github.ramboxeu.techworks.common.recipe.HammeringRecipe;
import io.github.ramboxeu.techworks.common.registration.TechworksItems;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class AnvilIngotHolderTile extends BaseTechworksTile {
    private final SingleStackStorage storage;
    private final RecipeWrapper recipeInv;
    private HammeringRecipe cachedRecipe;
    private int hits;

    public AnvilIngotHolderTile() {
        super(TechworksTiles.ANVIL_INGOT_HOLDER.get());
        storage = new SingleStackStorage(ItemStack.EMPTY);
        recipeInv = new RecipeWrapper(storage);
    }

    public ItemStack getStack() {
        return storage.getStackInSlot(0);
    }

    @Override
    protected CompoundNBT writeUpdateTag(CompoundNBT tag) {
        tag.put("Storage", storage.serializeNBT());
        tag.putInt("Hits", hits);
        return super.writeUpdateTag(tag);
    }

    @Override
    protected void readUpdateTag(CompoundNBT tag, BlockState state) {
        super.readUpdateTag(tag, state);
        storage.deserializeNBT(tag.getCompound("Storage"));
        hits = tag.getInt("Hits");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("Storage", storage.serializeNBT());
        tag.putInt("Hits", hits);
        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        storage.deserializeNBT(tag.getCompound("Storage"));
        hits = tag.getInt("Hits");
    }

    @Override
    public void setWorldAndPos(World world, BlockPos pos) {
        super.setWorldAndPos(world, pos);
        cachedRecipe = world.getRecipeManager().getRecipe(TechworksRecipes.HAMMERING.get(), recipeInv, world).orElse(null);
    }

    public void setStack(ItemStack stack) {
        this.storage.setStackInSlot(0, stack);
        cachedRecipe = world.getRecipeManager().getRecipe(TechworksRecipes.HAMMERING.get(), recipeInv, world).orElse(null);
    }

    public void onHammerHit(PlayerEntity player) {
        hits++;

        if (cachedRecipe != null && hits == cachedRecipe.getHits()) {
            Item item = storage.getStack().getItem();
            storage.setStack(cachedRecipe.getCraftingResult(recipeInv));
            hits = 0;

            ItemStack offhandItem = player.getHeldItemOffhand();
            if (offhandItem.getItem() == item) {
                int count = player.isSneaking() ? 4 : 2;

                if (offhandItem.getCount() >= count) {
                    dropStack();
                    player.swing(Hand.OFF_HAND, false);
                    storage.setStack(offhandItem.split(count));
                    return;
                }
            }

            AnvilIngotHolderBlock.take(world, pos);
        }
    }

    public void dropStack() {
        ItemEntity item = new ItemEntity(world, pos.getX() + .25, pos.getY() + .25, pos.getZ() + .25, storage.getStack());
        item.setDefaultPickupDelay();
        world.addEntity(item);
    }

    public boolean addItems(PlayerEntity player) {
        if (player.isSneaking()) {
            ItemStack handStack = player.getHeldItemMainhand();
            ItemStack offhandStack = player.getHeldItemOffhand();

            if (storage.getStack().getCount() < 4) {
                if (handStack.isItemEqual(storage.getStack()))
                    return storage.insertItem(0, handStack.split(2), false).isEmpty();

                if (offhandStack.isItemEqual(storage.getStack()))
                    return storage.insertItem(0, offhandStack.split(2), false).isEmpty();
            }
        }

        return false;
    }
}
