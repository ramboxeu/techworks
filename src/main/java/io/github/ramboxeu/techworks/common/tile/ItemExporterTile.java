package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksFluids;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.util.Side;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemExporterTile extends BaseIOTile {
    private final ItemStackHandler inv = new ItemStackHandler(1);
    private final LazyOptional<IItemHandler> invHolder = LazyOptional.of(() -> inv);

    private int counter = 0;

    public ItemExporterTile() {
        super(TechworksTiles.ITEM_EXPORTER_TILE.getTileType());

//        trackOptional(invHolder);

        invHolder.addListener(holder -> {
            Techworks.LOGGER.debug("Invalidated invHolder!");
        });
    }

    @Override
    protected void serverTick() {
        if (counter == 10) {
            for (Direction direction : Direction.values()) {
                TileEntity tile = world.getTileEntity(pos.offset(direction));

                if (tile != null) {
                    checkEnergy(tile, direction);
                    checkLiquid(tile, direction);
                    checkGas(tile, direction);
                }

                int a = 20;
                Techworks.LOGGER.debug("Test msg: {}", a);
            }

//                te.getCapability(CapabilityEnergy.ENERGY, Direction.EAST).ifPresent(handler -> {
//                    Techworks.LOGGER.debug(handler);
//                    Techworks.LOGGER.debug("Received = {}", handler.receiveEnergy(300, true));
//                    Techworks.LOGGER.debug("Extracted = {}", handler.receiveEnergy(150, true));
//                    Techworks.LOGGER.debug("Stored = {}, MaxStorage = {}", handler.getEnergyStored(), handler.getMaxEnergyStored());
//                });

//                te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.EAST).ifPresent(handler -> {
//                    Techworks.LOGGER.debug(handler);
////                    Techworks.LOGGER.debug("Filled: {}", handler.fill(new FluidStack(Fluids.WATER, 300), IFluidHandler.FluidAction.EXECUTE));
////                    Techworks.LOGGER.debug("Drained: {}", DevUtils.fluidStackToString(handler.drain(new FluidStack(Fluids.LAVA, 300), IFluidHandler.FluidAction.EXECUTE)));
//                    Techworks.LOGGER.debug("Drained: {}", DevUtils.fluidStackToString(handler.drain(567, IFluidHandler.FluidAction.EXECUTE)));
//
//                    for (int i = 0; i < handler.getTanks(); i++) {
//                        Techworks.LOGGER.debug("#{} Stored: {}", i, DevUtils.fluidStackToString(handler.getFluidInTank(i)));
//                    }
//                });
            counter = 0;
        } else {
            counter++;
        }
    }

    public void checkLiquid(TileEntity tile, Direction direction) {
        tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).ifPresent(handler -> {
            handler.fill(new FluidStack(Fluids.WATER, 100), IFluidHandler.FluidAction.EXECUTE);
            handler.fill(new FluidStack(Fluids.LAVA, 555), IFluidHandler.FluidAction.SIMULATE);
            handler.drain(new FluidStack(Fluids.WATER, 111), IFluidHandler.FluidAction.EXECUTE);
            handler.drain(new FluidStack(Fluids.LAVA, 222), IFluidHandler.FluidAction.SIMULATE);
            testLiquidHandler(handler);
        });
    }

    public void checkGas(TileEntity tile, Direction direction) {
        tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).ifPresent(handler -> {
            handler.fill(new FluidStack(TechworksFluids.STEAM.getKey().get(), 100), IFluidHandler.FluidAction.EXECUTE);
            handler.drain(new FluidStack(TechworksFluids.STEAM.getKey().get(), 222), IFluidHandler.FluidAction.SIMULATE);
            testGasHandler(handler);
        });
    }

    public void checkEnergy(TileEntity tile, Direction direction) {
        tile.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(handler -> {
            handler.extractEnergy(100, false);
            handler.extractEnergy(320, true);
            handler.receiveEnergy(323, false);
            handler.receiveEnergy(121, true);
            testEnergyHandler(handler);
        });
    }

    private void testLiquidHandler(IFluidHandler handler) {

    }

    private void testGasHandler(IFluidHandler handler) {

    }

    private void testEnergyHandler(IEnergyStorage storage) {

    }

    @Nullable
    @Override
    protected <T> LazyOptional<T> getFallbackCapability(@NotNull Capability<T> cap, Side side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return invHolder.cast();
        }

        return null;
    }
}
