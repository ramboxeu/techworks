package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.api.component.ComponentStackHandler;
import io.github.ramboxeu.techworks.client.container.machine.ComponentsContainer;
import io.github.ramboxeu.techworks.common.tile.machine.MachineIO;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public abstract class BaseMachineTile extends BaseTechworksTile implements INamedContainerProvider {
    protected MachineIO machineIO;
    protected final ComponentStackHandler components;

    public BaseMachineTile(TileEntityType<?> tileEntityType, ComponentStackHandler.Builder builder) {
        super(tileEntityType);

        this.components = ComponentStackHandler.withBuilder(builder.onChanged(this::refreshComponents));
    }

    @Override
    protected void onFirstTick() {
        if (!world.isRemote && machineIO != null) {
            machineIO.setFaceStatus(world.getBlockState(pos).get(BlockStateProperties.HORIZONTAL_FACING), true);
        }
    }

    // PASS continues the execution on the block side
    public ActionResultType onRightClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return ActionResultType.PASS;
    }

    public void onLeftClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {}

    protected void refreshComponents(ItemStack stack, boolean input) {}

    public List<ItemStack> getDrops() {
        return Collections.emptyList();
    }

    protected abstract ITextComponent getComponentsGuiName();

    public MachineIO getMachineIO() {
        return machineIO;
    }

    @Override
    protected void readUpdateTag(CompoundNBT nbt, BlockState state) {
        machineIO.deserializeNBT(nbt.getCompound("MachineIO"));

        super.readUpdateTag(nbt, state);
    }

    @Override
    protected CompoundNBT writeUpdateTag(CompoundNBT nbt) {
        nbt.put("MachineIO", machineIO.serializeNBT());

        return super.writeUpdateTag(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("ComponentInv", components.serializeNBT());
        compound.put("MachineIO", machineIO.serializeNBT());

        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        components.deserializeNBT(compound.getCompound("ComponentInv"));
        machineIO.deserializeNBT(compound.getCompound("MachineIO"));

        super.read(state, compound);
    }

    public void openComponentsScreen(ServerPlayerEntity player) {
        NetworkHooks.openGui(player, new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return getComponentsGuiName();
            }

            @Nullable
            @Override
            public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
                return new ComponentsContainer(id, inv, components);
            }
        }, pos);
    }

    public ComponentStackHandler getComponents() {
        return components;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> holder = machineIO.getCapability(cap, side);
        return holder != null ? holder : super.getCapability(cap, side);
    }
}
