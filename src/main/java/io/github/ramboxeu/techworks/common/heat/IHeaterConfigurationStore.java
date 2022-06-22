package io.github.ramboxeu.techworks.common.heat;

import io.github.ramboxeu.techworks.common.fluid.handler.IGasTank;
import io.github.ramboxeu.techworks.common.fluid.handler.ILiquidTank;
import io.github.ramboxeu.techworks.common.util.machineio.data.EnergyHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.GasHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.ItemHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.LiquidHandlerData;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import java.util.Collection;
import java.util.function.Supplier;

public interface IHeaterConfigurationStore {
    ItemHandlerData getHandlerData(IItemHandler handler);
    ItemHandlerData getHandlerData(IItemHandler handler, int flags);
    LiquidHandlerData getHandlerData(ILiquidTank tank);
    LiquidHandlerData getHandlerData(ILiquidTank tank, int flags);
    GasHandlerData getHandlerData(IGasTank tank);
    GasHandlerData getHandlerData(IGasTank tank, int flags);
    EnergyHandlerData getHandlerData(IEnergyStorage storage);
    EnergyHandlerData getHandlerData(IEnergyStorage storage, int flags);
    void setHandlerStorageFlags(int flags);
    void setDropsSupplier(Supplier<Collection<ItemStack>> drops);
}
