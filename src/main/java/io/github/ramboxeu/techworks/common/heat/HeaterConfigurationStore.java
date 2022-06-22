package io.github.ramboxeu.techworks.common.heat;

import io.github.ramboxeu.techworks.common.fluid.handler.IGasTank;
import io.github.ramboxeu.techworks.common.fluid.handler.ILiquidTank;
import io.github.ramboxeu.techworks.common.util.machineio.MachineIO;
import io.github.ramboxeu.techworks.common.util.machineio.data.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import java.util.*;
import java.util.function.Supplier;

public class HeaterConfigurationStore implements IHeaterConfigurationStore {
    private final MachineIO machineIO;
    private final List<HandlerData> dataList = new ArrayList<>();
    private int handlerStorageFlags = 0;
    private Supplier<Collection<ItemStack>> drops = Collections::emptyList;

    public HeaterConfigurationStore(MachineIO machineIO) {
        this.machineIO = machineIO;
    }

    @Override
    public void setHandlerStorageFlags(int flags) {
        handlerStorageFlags = flags;
    }

    // TODO: The same handler can be added multiple times (do not do this)
    @Override
    public ItemHandlerData getHandlerData(IItemHandler handler) {
        Optional<HandlerData> optional = dataList.stream().filter(data -> data.getObject() == handler).findAny();
        if (optional.isPresent())
            return (ItemHandlerData) optional.get();

        ItemHandlerData data = machineIO.getHandlerData(handler);
        dataList.add(data);
        return data;
    }

    @Override
    public ItemHandlerData getHandlerData(IItemHandler handler, int flags) {
        Optional<HandlerData> optional = dataList.stream().filter(data -> data.getObject() == handler).findAny();
        if (optional.isPresent()) {
            return (ItemHandlerData) optional.get();
        }

        ItemHandlerData data = machineIO.getHandlerData(handler, flags);
        dataList.add(data);
        return data;
    }

    @Override
    public LiquidHandlerData getHandlerData(ILiquidTank tank) {
        Optional<HandlerData> optional = dataList.stream().filter(data -> data.getObject() == tank).findAny();
        if (optional.isPresent())
            return (LiquidHandlerData) optional.get();

        LiquidHandlerData data = machineIO.getHandlerData(tank);
        dataList.add(data);
        return data;
    }

    @Override
    public LiquidHandlerData getHandlerData(ILiquidTank tank, int flags) {
        LiquidHandlerData data = machineIO.getHandlerData(tank, flags);
        dataList.add(data);
        return data;
    }

    @Override
    public GasHandlerData getHandlerData(IGasTank tank) {
        Optional<HandlerData> optional = dataList.stream().filter(data -> data.getObject() == tank).findAny();
        if (optional.isPresent())
            return (GasHandlerData) optional.get();

        GasHandlerData data = machineIO.getHandlerData(tank);
        dataList.add(data);
        return data;
    }

    @Override
    public GasHandlerData getHandlerData(IGasTank tank, int flags) {
        GasHandlerData data = machineIO.getHandlerData(tank, flags);
        dataList.add(data);
        return data;
    }

    @Override
    public EnergyHandlerData getHandlerData(IEnergyStorage storage) {
        Optional<HandlerData> optional = dataList.stream().filter(data -> data.getObject() == storage).findAny();
        if (optional.isPresent())
            return (EnergyHandlerData) optional.get();

        EnergyHandlerData data = machineIO.getHandlerData(storage);
        dataList.add(data);
        return data;
    }

    @Override
    public EnergyHandlerData getHandlerData(IEnergyStorage storage, int flags) {
        EnergyHandlerData data = machineIO.getHandlerData(storage, flags);
        dataList.add(data);
        return data;
    }

    @Override
    public void setDropsSupplier(Supplier<Collection<ItemStack>> drops) {
        this.drops = drops;
    }

    public void clear() {
        handlerStorageFlags = 0;
        drops = Collections::emptyList;
        machineIO.removeHandlers(dataList);
    }

    public Collection<ItemStack> getDrops() {
        return drops.get();
    }

    public int getHandlerStorageFlags() {
        return handlerStorageFlags;
    }
}
