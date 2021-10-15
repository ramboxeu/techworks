package io.github.ramboxeu.techworks.common.util.machineio;

import com.google.common.collect.Streams;
import io.github.ramboxeu.techworks.client.util.Color;
import io.github.ramboxeu.techworks.common.energy.EnergyHandlerContainer;
import io.github.ramboxeu.techworks.common.fluid.handler.FluidHandlerContainer;
import io.github.ramboxeu.techworks.common.fluid.handler.IGasTank;
import io.github.ramboxeu.techworks.common.fluid.handler.ILiquidTank;
import io.github.ramboxeu.techworks.common.item.handler.ItemHandlerContainer;
import io.github.ramboxeu.techworks.common.util.Side;
import io.github.ramboxeu.techworks.common.util.function.TriConsumer;
import io.github.ramboxeu.techworks.common.util.machineio.config.EnergyHandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.config.FluidHandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.config.ItemHandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.data.*;
import io.github.ramboxeu.techworks.common.util.machineio.handler.IHandlerContainer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MachineIO implements ICapabilityProvider, INBTSerializable<CompoundNBT> {
    public static final MachineIO DISABLED = new MachineIO();

    private final Supplier<Direction> facing;
    private final MainMapper mainMapper;

    private final List<ItemHandlerData> itemHandlerPool;
    private final List<LiquidHandlerData> liquidTankPool;
    private final List<GasHandlerData> gasTankPool;
    private final List<EnergyHandlerData> energyStoragePool;

    private int disabledSides;
    private int lastColor = -10;

    private final MachinePort[] ports = new MachinePort[]{
            MachinePort.DISABLED, // Internal
            MachinePort.DISABLED, // Bottom
            MachinePort.DISABLED, // Top
            MachinePort.DISABLED, // Front
            MachinePort.DISABLED, // Right
            MachinePort.DISABLED, // Back
            MachinePort.DISABLED, // Left
    };

    private MachineIO() {
        facing = () -> Direction.NORTH;
        mainMapper = (side, cap) -> null;
        disabledSides = 127;
        itemHandlerPool = Collections.emptyList();
        liquidTankPool = Collections.emptyList();
        gasTankPool = Collections.emptyList();
        energyStoragePool = Collections.emptyList();
    }

    public MachineIO(Supplier<Direction> facing, MainMapper mainMapper) {
        this.facing = facing;
        this.mainMapper = mainMapper;
        itemHandlerPool = new ArrayList<>();
        liquidTankPool = new ArrayList<>();
        gasTankPool = new ArrayList<>();
        energyStoragePool = new ArrayList<>();
        disabledSides = 0;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return getTileCapability(cap, Side.fromDirection(side, facing.get()), null);
    }

    public <T> LazyOptional<T> getTileCapability(Capability<T> cap, Direction side, @Nullable ICapabilityProvider parent) {
        return getTileCapability(cap, Side.fromDirection(side, facing.get()), parent);
    }

    /*
     * Gets an optional representing a capability. Also includes holding TE's caps
     */
    public <T> LazyOptional<T> getTileCapability(Capability<T> cap, Side side, @Nullable ICapabilityProvider parent) {
        LazyOptional<?> main = mainMapper.getCapability(cap, side);

        if (main != null) {
            return main.cast();
        }

        LazyOptional<?> holder = ports[side.getIndex()].getHolder(InputType.fromCap(cap));

        if (holder != null) {
            return holder.cast();
        }

        return parent != null ? parent.getCapability(cap, side.toDirection(facing.get())) : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putByte("DisabledSides", (byte) disabledSides);

//        Marker marker = MarkerManager.getMarker(String.valueOf(hashCode()));
//        for (Side side : Side.values()) {
//            MachinePort port = ports[side.getIndex()];
//
//            if (!port.isDisabled()) {
//                LazyOptional<IItemHandler> holder = port.getItemHolder();
//                List<HandlerConfig> list = holder.<List<HandlerConfig>>map(handler -> handler instanceof IHandlerContainer ?
//                                ((IHandlerContainer) handler).getConfigs() :
//                                Collections.emptyList()
//                ).orElse(Collections.emptyList());

//                if (!list.isEmpty()) {
//                    Techworks.LOGGER.debug(marker, "Side {} : Type ITEM : {}", side, list.toString());
//                } else {
//                    Techworks.LOGGER.debug(marker, "Side {} : Type ITEM : empty", side);
//                }

//                LazyOptional<IEnergyStorage> energyHolder = port.getEnergyHolder();
//                List<HandlerConfig> energyList = energyHolder.<List<HandlerConfig>>map(handler -> handler instanceof IHandlerContainer ?
//                        ((IHandlerContainer) handler).getConfigs() :
//                        Collections.emptyList()
//                ).orElse(Collections.emptyList());

//                if (!energyList.isEmpty()) {
//                    Techworks.LOGGER.debug(marker, "Side {} : Type ENERGY : {}", side, energyList.toString());
//                } else {
//                    Techworks.LOGGER.debug(marker, "Side {} : Type ENERGY : empty", side);
//                }
//            } else {
//                Techworks.LOGGER.debug(marker, "Side {} : disabled", side);
//            }
//        }

        Map<Side, List<HandlerConfig>> map = createDataMap();

        CompoundNBT mapTag = new CompoundNBT();
        for (Map.Entry<Side, List<HandlerConfig>> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                List<HandlerConfig> list = entry.getValue();
                ListNBT listTag = new ListNBT();

                for (int i = 0; i < list.size(); i++) {
                    HandlerConfig config = list.get(i);
                    CompoundNBT configTag = new CompoundNBT();
                    configTag.putInt("Id", config.getBaseData().getIdentity());
                    configTag.putString("Mode", config.getMode().name());
                    configTag.putString("Type", config.getBaseData().getType().name());

                    listTag.add(i, configTag);
                }

                mapTag.put(entry.getKey().name(), listTag);
            }
        }
        tag.put("ConfigMap", mapTag);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        disabledSides = tag.getByte("DisabledSides");

        CompoundNBT mapTag = tag.getCompound("ConfigMap");
        for (Side side : Side.values()) {
            ListNBT listTag = mapTag.getList(side.name(), Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < listTag.size(); i++) {
                CompoundNBT configTag = listTag.getCompound(i);
                int id = configTag.getInt("Id");
                StorageMode mode = StorageMode.valueOf(configTag.getString("Mode"));
                InputType type = InputType.valueOf(configTag.getString("Type"));

                // Prevent duplicate configs caused by adding data before deserialization
                switch (type) {
                    case ITEM:
                        addHandlerData(side, itemHandlerPool.get(id), mode);
                        break;
                    case LIQUID:
                        addHandlerData(side, liquidTankPool.get(id), mode);
                        break;
                    case GAS:
                        addHandlerData(side, gasTankPool.get(id), mode);
                        break;
                    case ENERGY:
                        addHandlerData(side, energyStoragePool.get(id), mode);
                        break;
                }
            }
        }
    }

    private int getColor() {
        return Color.HSVtoRGB((lastColor += 10) / 360.0f, 0.7f, 0.84f);
    }

    public List<ItemHandlerData> getHandlersData(IItemHandler... handlers) {
        List<ItemHandlerData> list = new ArrayList<>(handlers.length);

        for (IItemHandler handler : handlers) {
            ItemHandlerData data = new ItemHandlerData(null, InputType.ITEM, getColor(), itemHandlerPool.size(), handler, 0, handler.getSlots() - 1);
            list.add(data);
            itemHandlerPool.add(data);
        }

        return list;
    }

    public ItemHandlerData getHandlerData(IItemHandler handler) {
        ItemHandlerData data = new ItemHandlerData(null, InputType.ITEM, getColor(), itemHandlerPool.size(), handler, 0, handler.getSlots() - 1);

        itemHandlerPool.add(data);
        return data;
    }

    public ItemHandlerData getHandlerData(IItemHandler handler, int minSlot, int maxSlot) {
        ItemHandlerData data = new ItemHandlerData(null, InputType.ITEM, getColor(), itemHandlerPool.size(), handler, minSlot, maxSlot);

        itemHandlerPool.add(data);
        return data;
    }

    public List<LiquidHandlerData> getHandlersData(ILiquidTank... tanks) {
        List<LiquidHandlerData> list = new ArrayList<>(tanks.length);

        for (ILiquidTank tank : tanks) {
            LiquidHandlerData data = new LiquidHandlerData(InputType.LIQUID, getColor(), liquidTankPool.size(), tank);
            list.add(data);
            liquidTankPool.add(data);
        }

        return list;
    }

    public LiquidHandlerData getHandlerData(ILiquidTank tank) {
        LiquidHandlerData data = new LiquidHandlerData(InputType.LIQUID, getColor(), liquidTankPool.size(), tank);

        liquidTankPool.add(data);
        return data;
    }

    public List<GasHandlerData> getHandlersData(IGasTank... tanks) {
        List<GasHandlerData> list = new ArrayList<>(tanks.length);

        for (IGasTank tank : tanks) {
            GasHandlerData data = new GasHandlerData(InputType.GAS, getColor(), -1, tank);
            list.add(data);
            gasTankPool.add(data);
        }

        return list;
    }

    public GasHandlerData getHandlerData(IGasTank tank) {
        GasHandlerData data = new GasHandlerData(InputType.GAS, getColor(), gasTankPool.size(), tank);

        gasTankPool.add(data);
        return data;
    }

    public List<EnergyHandlerData> getHandlersData(IEnergyStorage... storages) {
        List<EnergyHandlerData> list = new ArrayList<>(storages.length);

        for (IEnergyStorage storage : storages) {
            EnergyHandlerData data = new EnergyHandlerData(InputType.ENERGY, getColor(), energyStoragePool.size(), storage);
            list.add(data);
            energyStoragePool.add(data);
        }

        return list;
    }

    public EnergyHandlerData getHandlerData(IEnergyStorage storage) {
        EnergyHandlerData data = new EnergyHandlerData(InputType.ENERGY, getColor(), energyStoragePool.size(), storage);

        energyStoragePool.add(data);
        return data;
    }

    private MachinePort getPortInternal(Side side) {
        int index = side.getIndex();
        MachinePort port = ports[index];

        if (port.isDisabled()) {
            return ports[index] = new MachinePort();
        } else {
            return port;
        }
    }

    private <T> T getContainer(Class<T> clazz, MachinePort port, InputType type, Supplier<T> instance) {
        LazyOptional<?> holder = port.getHolder(type);

        final T handler;

        if (holder == null || !holder.isPresent()) {
            handler =  instance.get();
        } else {
            Object held = holder.orElse(null);

            if (held.getClass() == clazz) {
                return clazz.cast(held);
            } else {
                holder.invalidate();
                handler = instance.get();
            }
        }

        port.setHolder(LazyOptional.of(() -> handler), type);
        return handler;
    }

    public void addHandlersData(Side side, ItemHandlerData ...itemsData) {
        MachinePort port = getPortInternal(side);
        ItemHandlerContainer container = getContainer(ItemHandlerContainer.class, port, InputType.ITEM, ItemHandlerContainer::new);
        List<ItemHandlerConfig> configs = Arrays.stream(itemsData).map(ItemHandlerConfig::new).collect(Collectors.toList());

        container.addHandlers(configs);
    }

    public void addHandlersData(Side side, LiquidHandlerData ...tanksData) {
        MachinePort port = getPortInternal(side);
        FluidHandlerContainer container = getContainer(FluidHandlerContainer.class, port, InputType.LIQUID, FluidHandlerContainer::new);
        List<FluidHandlerConfig> configs = Arrays.stream(tanksData).map(FluidHandlerConfig::new).collect(Collectors.toList());

        container.addHandlers(configs);
    }

    public void addHandlersData(Side side, GasHandlerData ...tanksData) {
        MachinePort port = getPortInternal(side);
        FluidHandlerContainer container = getContainer(FluidHandlerContainer.class, port, InputType.GAS, FluidHandlerContainer::new);
        List<FluidHandlerConfig> configs = Arrays.stream(tanksData).map(FluidHandlerConfig::new).collect(Collectors.toList());

        container.addHandlers(configs);
    }

    public void addHandlersData(Side side, EnergyHandlerData ...storagesData) {
        MachinePort port = getPortInternal(side);
        EnergyHandlerContainer container = getContainer(EnergyHandlerContainer.class, port, InputType.ENERGY, EnergyHandlerContainer::new);
        List<EnergyHandlerConfig> configs = Arrays.stream(storagesData).map(EnergyHandlerConfig::new).collect(Collectors.toList());

        container.addHandlers(configs);
    }

    public EnergyHandlerConfig addHandlerData(Side side, EnergyHandlerData data, StorageMode mode) {
        MachinePort port = getPortInternal(side);
        EnergyHandlerContainer container = getContainer(EnergyHandlerContainer.class, port, InputType.ENERGY, EnergyHandlerContainer::new);

        return container.addHandler(new EnergyHandlerConfig(mode, data));
    }

    public ItemHandlerConfig addHandlerData(Side side, ItemHandlerData data, StorageMode mode) {
        MachinePort port = getPortInternal(side);
        ItemHandlerContainer container = getContainer(ItemHandlerContainer.class, port, InputType.ITEM, ItemHandlerContainer::new);

        return container.addHandler(new ItemHandlerConfig(mode, data));
    }

    public FluidHandlerConfig addHandlerData(Side side, LiquidHandlerData data, StorageMode mode) {
        MachinePort port = getPortInternal(side);
        FluidHandlerContainer container = getContainer(FluidHandlerContainer.class, port, InputType.LIQUID, FluidHandlerContainer::new);

        return container.addHandler(new FluidHandlerConfig(mode, data));
    }

    public FluidHandlerConfig addHandlerData(Side side, GasHandlerData data, StorageMode mode) {
        MachinePort port = getPortInternal(side);
        FluidHandlerContainer container = getContainer(FluidHandlerContainer.class, port, InputType.GAS, FluidHandlerContainer::new);

        return container.addHandler(new FluidHandlerConfig(mode, data));
    }

    /*
     * public void addHandlerData(EnergyHandlerData data, Side ...sides)
     */

    private <T extends HandlerData, U extends IHandlerContainer> void addOrModifyConfig(Side side, InputType type, StorageMode mode, T data, TriConsumer<Side, T, StorageMode> dataAdder, Class<U> clazz) {
        MachinePort port = getPort(side);
        LazyOptional<?> holder = port.getHolder(type);

        if (holder == null || !holder.isPresent()) {
            dataAdder.accept(side, data, mode);
        } else {
            Object held = holder.orElse(null);

            if (held.getClass() == clazz) {
                clazz.cast(held).setStorageMode(data, mode);
            } else {
                dataAdder.accept(side, data, mode);
            }
        }
    }

    public void setHandlerConfigMode(int poolIndex, Side side, StorageMode mode, InputType type) {
        switch (type) {
            case ITEM: {
                if (itemHandlerPool.size() <= poolIndex) return;
                ItemHandlerData data = itemHandlerPool.get(poolIndex);
                addOrModifyConfig(side, type, mode, data, this::addHandlerData, ItemHandlerContainer.class);
                break;
            }
            case LIQUID: {
                if (liquidTankPool.size() <= poolIndex) return;
                LiquidHandlerData data = liquidTankPool.get(poolIndex);
                addOrModifyConfig(side, type, mode, data, this::addHandlerData, FluidHandlerContainer.class);
                break;
            }
            case GAS: {
                if (gasTankPool.size() <= poolIndex) return;
                GasHandlerData data = gasTankPool.get(poolIndex);
                addOrModifyConfig(side, type, mode, data, this::addHandlerData, FluidHandlerContainer.class);
                break;
            }
            case ENERGY: {
                if (energyStoragePool.size() <= poolIndex) return;
                EnergyHandlerData data = energyStoragePool.get(poolIndex);
                addOrModifyConfig(side, type, mode, data, this::addHandlerData, EnergyHandlerContainer.class);
                break;
            }
        }
    }

    public HandlerConfig setConfigStatus(int poolIndex, Side side, InputType type, StorageMode mode, boolean enabled) {
        switch (type) {
            case ITEM: {
                ItemHandlerData data = itemHandlerPool.get(poolIndex);
                return enabled ? addHandlerData(side, data, mode) : removeHandler(side, data);
            }
            case LIQUID: {
                LiquidHandlerData data = liquidTankPool.get(poolIndex);
                return enabled ? addHandlerData(side, data, mode) : removeHandler(side, data);
            }
            case GAS: {
                GasHandlerData data = gasTankPool.get(poolIndex);
                return enabled ? addHandlerData(side, data, mode) : removeHandler(side, data);
            }
            case ENERGY: {
                EnergyHandlerData data = energyStoragePool.get(poolIndex);
                return enabled ? addHandlerData(side, data, mode) : removeHandler(side, data);
            }
        }

        return null;
    }

    private IHandlerContainer getContainerForRemoval(Side side, InputType type) {
        LazyOptional<?> holder = ports[side.getIndex()].getHolder(type);

        if (holder != null) {
            Object held = holder.orElse(null);

            if (held instanceof IHandlerContainer) {
                return (IHandlerContainer) held;
            }
        }

        return null;
    }

    public void removeHandlers(Side side, HandlerData...metadata) {
        for (HandlerData data : metadata) {
            IHandlerContainer container = getContainerForRemoval(side, data.getType());

            if (container != null) {
                container.remove(data);
            }
        }
    }

    public HandlerConfig removeHandler(Side side, HandlerData data) {
        IHandlerContainer container = getContainerForRemoval(side, data.getType());

        if (container != null) {
            return container.remove(data);
        }

        return null;
    }

    public List<HandlerConfig> getHandlerConfigs(Side side) {
        MachinePort port = ports[side.getIndex()];

        ArrayList<HandlerConfig> list = new ArrayList<>();
        list.addAll(port.getItemConfigs());
        list.addAll(port.getEnergyConfigs());

        return list;
    }

    @Nullable
    public HandlerConfig getHandlerConfig(Side side, HandlerData data) {
        IHandlerContainer container = getContainerForRemoval(side, data.getType());

        if (container != null) {
            Optional<HandlerConfig> config = container.getConfigs().stream().filter(c -> c.getBaseData() == data).findFirst();
            return config.orElse(null);
        }

        return null;
    }

    private <T> Stream<HandlerConfig> getDataStream(Class<T> clazz, MachinePort port, InputType type, Function<T, List<? extends HandlerConfig>> config) {
        LazyOptional<?> holder = port.getHolder(type);

        if (holder != null && holder.isPresent()) {
            Object held = holder.orElse(null);

            if (held.getClass() == clazz) {
                return config.apply(clazz.cast(held)).stream().map(c -> c);
            }
        }

        return Stream.empty();
    }

    @Deprecated
    public Map<Side, List<HandlerConfig>> createDataMap() {
        Map<Side, List<HandlerConfig>> map = new EnumMap<>(Side.class);

        for (Side side : Side.values()) {
            MachinePort port = ports[side.getIndex()];

            if (!port.isDisabled()) {
                map.put(side, Streams.concat(
                        getDataStream(ItemHandlerContainer.class, port, InputType.ITEM, IHandlerContainer::getConfigs),
                        getDataStream(FluidHandlerContainer.class, port, InputType.LIQUID, IHandlerContainer::getConfigs),
                        getDataStream(FluidHandlerContainer.class, port, InputType.GAS, IHandlerContainer::getConfigs),
                        getDataStream(EnergyHandlerContainer.class, port, InputType.ENERGY, IHandlerContainer::getConfigs)
                ).collect(Collectors.toList()));
            }
        }

        return map;
    }

    public void setHolder(Side side, InputType type, LazyOptional<?> holder) {
        LazyOptional<?> oldHolder = ports[side.getIndex()].getHolder(type);

        if (oldHolder != null && oldHolder.isPresent()) {
            oldHolder.invalidate();
        }

        ports[side.getIndex()].setHolder(holder, type);
    }

    public void setSideDisabled(Side side, boolean disabled) {
        if (disabled) {
            disabledSides |= side.getBinIndex();

            MachinePort port = ports[side.getIndex()];
            if (!port.isDisabled()) {
                port.invalidate();
            }

            ports[side.getIndex()] = MachinePort.DISABLED;
        } else {
            disabledSides &= ~side.getBinIndex();

            MachinePort port = ports[side.getIndex()];
            if (port.isDisabled()) {
                ports[side.getIndex()] = new MachinePort();
            }
        }
    }

    public boolean isSideDisabled(Side side) {
        int i = side.getBinIndex();
        return (disabledSides & i) == i;
    }

    public MachinePort getPort(Direction direction) {
        return getPort(Side.fromDirection(direction, facing.get()));
    }

    public MachinePort getPort(Side side) {
        return ports[side.getIndex()];
    }

    public boolean isDisabled() {
        return this == DISABLED;
    }

    public void invalidateCaps() {
        Arrays.asList(ports).forEach(MachinePort::invalidate);
    }
}