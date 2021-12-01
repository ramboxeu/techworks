package io.github.ramboxeu.techworks.common.item;

import io.github.ramboxeu.techworks.common.tag.TechworksItemTags;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum TechworksItemTier implements IItemTier {
    STEEL(780, 7.0f, 2.5f, 3, 12, () -> Ingredient.fromTag(TechworksItemTags.STEEL_INGOTS));

    private final int uses;
    private final float efficiency;
    private final float damage;
    private final int level;
    private final int enchantability;
    private final LazyValue<Ingredient> material;

    TechworksItemTier(int uses, float efficiency, float damage, int level, int enchantability, Supplier<Ingredient> material) {
        this.uses = uses;
        this.efficiency = efficiency;
        this.damage = damage;
        this.level = level;
        this.enchantability = enchantability;
        this.material = new LazyValue<>(material);
    }

    @Override
    public int getMaxUses() {
        return uses;
    }

    @Override
    public float getEfficiency() {
        return efficiency;
    }

    @Override
    public float getAttackDamage() {
        return damage;
    }

    @Override
    public int getHarvestLevel() {
        return level;
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return material.getValue();
    }
}
