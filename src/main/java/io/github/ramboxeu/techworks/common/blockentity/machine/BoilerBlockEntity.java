package io.github.ramboxeu.techworks.common.blockentity.machine;

import io.github.ramboxeu.techworks.Techworks;
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
    private ItemStack component = new ItemStack(TechworksItems.BASIC_BOILING_COMPONENT);
    private int lastSlot = 0;

    public BoilerBlockEntity() {
        super(TechworksBlockEntities.BOILER);

        componentList = new ComponentInventory<>(this, 5);
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
        tag.put("ComponentList", componentList.toTag());
        return super.toTag(tag);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        componentList.fromTag(tag.getCompound("ComponentList"));
        super.fromTag(tag);
    }

    public void onRightClick(boolean isSneaking) {
        //Techworks.LOG.info("Components List tag: {}", componentList.toTag());
        if (!isSneaking) {
            lastSlot++;
            componentList.setInvStack(lastSlot, component);
            Techworks.LOG.info(lastSlot);
        } else {
            Techworks.LOG.info(lastSlot - 1);
            Techworks.LOG.info(componentList.removeInvStack(lastSlot - 1));
            lastSlot--;
        }
        //Techworks.LOG.info("Components List tag: {}", componentList.toTag());
    }
}
