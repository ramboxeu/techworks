package io.github.ramboxeu.techworks.common.blockentity.machine;

import io.github.ramboxeu.techworks.client.widget.FluidTankWidget;
import io.github.ramboxeu.techworks.common.api.component.ComponentInventory;
import io.github.ramboxeu.techworks.common.api.widget.GasTankWidget;
import io.github.ramboxeu.techworks.common.container.ExtendedScreenHandlerProvider;
import io.github.ramboxeu.techworks.common.container.machine.BoilerContainer;
import io.github.ramboxeu.techworks.common.registry.ComponentTypes;
import io.github.ramboxeu.techworks.common.registry.TechworksBlockEntities;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BoilerBlockEntity extends AbstractMachineBlockEntity implements ExtendedScreenHandlerProvider {
    private int lastSlot = 0;

    public BoilerBlockEntity() {
        super(TechworksBlockEntities.BOILER);
    }

    @Override
    protected void buildMachinery(MachineryBuilder machineryBuilder) {
        super.buildMachinery(machineryBuilder);
        machineryBuilder.add(new ComponentInventory.Slot(ComponentTypes.FLUID_STORAGE_COMPONENT), i -> new FluidTankWidget(0, 0, 50, 50, i));
        machineryBuilder.add(new ComponentInventory.Slot(ComponentTypes.FLUID_STORAGE_COMPONENT), i -> new FluidTankWidget(0, 10, 50, 50, i));
        machineryBuilder.add(new ComponentInventory.Slot(ComponentTypes.FLUID_STORAGE_COMPONENT), i -> new FluidTankWidget(0, 20, 50, 50, i));
        machineryBuilder.add(new ComponentInventory.Slot(ComponentTypes.BOILING_COMPONENT), i -> new GasTankWidget(0, 40, 16, 52, i));
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        componentList.fromTag(tag.getCompound("ComponentList"));
        super.fromTag(state, tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.put("ComponentList", componentList.toTag());
        return super.toTag(tag);
    }

    @Override
    public ActionResult onActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
//        if (player.getMainHandStack().getItem().equals(Items.STICK)) {
//            ItemStack stack = new ItemStack(TechworksItems.BASIC_BOILING_COMPONENT);
//            if (componentList.isValidInvStack(lastSlot, stack)) {
//                this.componentList.setInvStack(lastSlot, stack);
//                lastSlot++;
//                return ActionResult.SUCCESS;
//            } else {
//                return ActionResult.FAIL;
//            }
//        }
//
//        if (player.getOffHandStack().getItem().equals(Items.STICK)) {
//            this.componentList.removeInvStack(lastSlot);
//            lastSlot--;
//            return ActionResult.SUCCESS;
//        }

        player.openHandledScreen(createMenu(state, world, pos));

        return super.onActivated(state, world, pos, player, hand, hit);
    }

    @Override
    public Text getComponentsContainerName() {
        return new TranslatableText("container.techworks.boiler_components");
    }

    @Override
    public ExtendedScreenHandlerFactory createMenu(BlockState state, World world, BlockPos pos) {
        return new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
                packetByteBuf.writeBlockPos(pos);
                packetByteBuf.writeInt(4);
            }

            @Override
            public Text getDisplayName() {
                return new LiteralText("bruh");
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new BoilerContainer(syncId, inv, BoilerBlockEntity.this, 4);
            }
        };
    }
}
