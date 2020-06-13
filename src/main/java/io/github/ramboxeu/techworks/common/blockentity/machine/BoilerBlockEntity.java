package io.github.ramboxeu.techworks.common.blockentity.machine;

import io.github.ramboxeu.techworks.common.api.component.ComponentInventory;
import io.github.ramboxeu.techworks.common.registry.TechworksBlockEntities;
import io.github.ramboxeu.techworks.common.registry.TechworksItems;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;

public class BoilerBlockEntity extends AbstractMachineBlockEntity<BoilerBlockEntity> {
    public BoilerBlockEntity() {
        super(TechworksBlockEntities.BOILER);

        componentList = new ComponentInventory<>(this, 5);
        componentList.setInvStack(0, new ItemStack(TechworksItems.BASIC_BOILING_COMPONENT));
    }

    @Override
    public Text getDisplayName() {
        return null;
    }

    @Override
    public Container createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return null;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.put("Components", componentList.toTag());
        return super.toTag(tag);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
    }
}
