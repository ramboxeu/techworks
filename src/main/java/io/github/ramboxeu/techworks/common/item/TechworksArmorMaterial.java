package io.github.ramboxeu.techworks.common.item;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.tag.TechworksItemTags;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.util.function.Supplier;

public enum TechworksArmorMaterial implements IArmorMaterial {
    STEEL("steel", 260, 300, 320, 220, 2, 5, 6, 2, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0f, 0.0f, () -> Ingredient.fromTag(TechworksItemTags.STEEL_INGOTS));

    private final String name;
    private final int[] durabilityFactors;
    private final int[] damageReductionFactors;
    private final int enchantability;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyValue<Ingredient> material;

    TechworksArmorMaterial(String name, int bootsHp, int leggingsHp, int chestHp, int helmetHp, int bootsDmg, int leggingsDmg, int chestDmg, int helmetDmg, int enchantability, SoundEvent sound, float toughness, float knockback, Supplier<Ingredient> material) {
        this.name = Techworks.MOD_ID + ":" + name;
        this.durabilityFactors = new int[] { bootsHp, leggingsHp, chestHp, helmetHp };
        this.damageReductionFactors = new int[] { bootsDmg, leggingsDmg, chestDmg, helmetDmg };
        this.enchantability = enchantability;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockback;
        this.material = new LazyValue<>(material);
    }

    @Override
    public int getDurability(EquipmentSlotType slot) {
        return durabilityFactors[slot.getIndex()];
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slot) {
        return damageReductionFactors[slot.getIndex()];
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return sound;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return material.getValue();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }
}
