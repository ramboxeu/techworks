package io.github.ramboxeu.techworks.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.ramboxeu.techworks.common.tile.AnvilIngotHolderTile;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HammerItem extends Item {
    private final Multimap<Attribute, AttributeModifier> attributes;

    public HammerItem(Properties properties) {
        super(properties.maxDamage(250));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> attributes = new ImmutableMultimap.Builder<>();
        attributes.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Hammer modifier", -3f, AttributeModifier.Operation.ADDITION));
        this.attributes = attributes.build();
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, PlayerEntity player) {
        World world = player.world;
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof AnvilIngotHolderTile) {
            if (player.getCooldownTracker().getCooldown(this, 0) > 0.0)
                return true;

            world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            ((AnvilIngotHolderTile) tile).onHammerHit(player);
            stack.damageItem(1, player, p -> {});
            player.getCooldownTracker().setCooldown(this, 16);
            return true;
        }

        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return slot == EquipmentSlotType.MAINHAND ? attributes : super.getAttributeModifiers(slot, stack);
    }
}
