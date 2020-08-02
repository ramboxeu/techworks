package io.github.ramboxeu.techworks.common.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.jetbrains.annotations.NotNull;

public abstract class BaseTechworksTile extends TileEntity implements ITickableTileEntity {
    private boolean firstTicked = false;

    public BaseTechworksTile(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void tick() {
        if (world != null) {
            if (!firstTicked) {
                onFirstTick();
                firstTicked = true;
            }

            if (world.isRemote) {
                clientTick();
            } else {
                serverTick();
            }
        }
    }

    // Method that gets called once and it should guarantee that world, pos are not null and tile with its chunk are loaded
    protected void onFirstTick() {}
    protected void serverTick() {}
    protected void clientTick() {}

    protected CompoundNBT writeUpdateTag(CompoundNBT nbt) {
        return nbt;
    }
    protected void readUpdateTag(CompoundNBT nbt, BlockState state) {
        super.read(state, nbt);
    }

    @Override
    public @NotNull CompoundNBT getUpdateTag() {
        return writeUpdateTag(super.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        readUpdateTag(tag, state);
    }
}
