package io.github.ramboxeu.techworks.common.blockentity.machine;

import io.github.ramboxeu.techworks.common.api.component.ComponentInventory;
import io.github.ramboxeu.techworks.common.api.widget.GasTankWidget;
import io.github.ramboxeu.techworks.common.api.widget.Widget;
import io.github.ramboxeu.techworks.common.registry.TechworksBlockEntities;
import io.github.ramboxeu.techworks.common.registry.TechworksItems;
import net.minecraft.block.BlockState;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BoilerBlockEntity extends AbstractMachineBlockEntity<BoilerBlockEntity> {
    private ItemStack component = new ItemStack(TechworksItems.BASIC_BOILING_COMPONENT);
    private int lastSlot = 0;

    public BoilerBlockEntity() {
        super(TechworksBlockEntities.BOILER);

        componentList = new ComponentInventory<>(this, 5);
        widgets = new ArrayList<Widget>() {
            {
                add(new GasTankWidget(0, 0, 16, 52, 0));
            }
        };
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

    @Override
    public ActionResult onActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getMainHandStack().getItem().equals(Items.STICK)) {
            if (hand == Hand.MAIN_HAND) {
                this.componentList.setInvStack(lastSlot, new ItemStack(TechworksItems.BASIC_BOILING_COMPONENT));
                lastSlot++;
            } else {
                this.componentList.removeInvStack(lastSlot);
                lastSlot--;
            }

            return ActionResult.SUCCESS;
        }

        return super.onActivated(state, world, pos, player, hand, hit);
    }
}
