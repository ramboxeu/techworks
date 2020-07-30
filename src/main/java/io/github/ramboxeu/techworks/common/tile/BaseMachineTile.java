package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.api.component.ComponentStackHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public abstract class BaseMachineTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    // TODO: Merge these 2 into nice util class
    protected Capability<?>[] capabilities = { null, null, null, null, null, null };
    protected LazyOptional<?>[] optionals = { LazyOptional.empty(), LazyOptional.empty(), LazyOptional.empty(), LazyOptional.empty(), LazyOptional.empty(), LazyOptional.empty()};
    protected final ComponentStackHandler components;

    public BaseMachineTile(TileEntityType<?> tileEntityType, ComponentStackHandler.Builder builder) {
        super(tileEntityType);

        this.components = ComponentStackHandler.withBuilder(builder.onChanged(() -> markComponentsDirty(false)));
    }

    // PASS continues the execution on the block side
    public ActionResultType onRightClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return ActionResultType.PASS;
    }

    public void onLeftClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {}

    protected void markComponentsDirty(boolean forced) {}

    @Override
    public void tick() {
        if (world != null) {
            if (world.isRemote) {
                clientTick();
            } else {
                serverTick();
            }
        }
    }

    protected void serverTick() {}

    protected void clientTick() {}

    public List<ItemStack> getDrops() {
        return Collections.emptyList();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (side != null) {
            Capability<?> capability = capabilities[side.getIndex()];

            if (cap == capability) {
                return optionals[side.getIndex()].cast();
            }
        }

        return super.getCapability(cap, side);
    }
}
