package io.github.ramboxeu.techworks.common.item;

import io.github.ramboxeu.techworks.common.recipe.WireCuttingRecipe;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.Optional;

public class WireCuttersItem extends Item {

    public WireCuttersItem(Properties properties) {
        super(properties.maxDamage(250));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);

        return new ActionResult<>(onRightClick(stack, player, hand, world), stack);
    }

    private ActionResultType onRightClick(ItemStack stack, PlayerEntity player, Hand hand, World world) {
        ItemStack ingredientStack = player.getHeldItem(getOppositeHand(hand));
        Inventory recipeInv = new Inventory(1);
        recipeInv.setInventorySlotContents(0, ingredientStack);

        Optional<WireCuttingRecipe> optional = world.getRecipeManager().getRecipe(TechworksRecipes.WIRE_CUTTING.get(), recipeInv, world);
        if (!optional.isPresent())
            return ActionResultType.PASS;

        WireCuttingRecipe recipe = optional.get();
        ingredientStack.shrink(recipe.getIngredientCount(ingredientStack));
        stack.damageItem(1, player, p -> p.sendBreakAnimation(hand));

        world.playSound(player, player.getPosition(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
        player.inventory.addItemStackToInventory(recipe.getCraftingResult(recipeInv));
        player.getCooldownTracker().setCooldown(this, 8);

        return ActionResultType.SUCCESS;
    }

    private static Hand getOppositeHand(Hand hand) {
        return hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
    }
}
