package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.client.screen.widget.config.ComponentsWidget;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.Side;
import io.github.ramboxeu.techworks.common.util.inventory.SlotBuilder;
import io.github.ramboxeu.techworks.common.util.machineio.AutoMode;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseMachineContainer<T extends BaseMachineTile> extends BaseInventoryContainer {
    protected T machineTile;
//    private int firstPlayerSlot = 0;

    protected final List<HandlerData> dataList;
    protected final ComponentsWidget componentsWidget;

    public BaseMachineContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, T machineTile) {
        super(containerType, playerInventory, id);

        dataList = new ArrayList<>();

        this.machineTile = machineTile;
        this.playerInventory = new InvWrapper(playerInventory);
        addPlayerListener(playerInventory.player);

        componentsWidget = addWidget(new ComponentsWidget(machineTile.getComponentStorage()));

        layoutPlayerSlots();
    }

    protected Slot addSlot(SlotBuilder slotBuilder) {
        return addSlot(slotBuilder.build());
    }

    public void changeStatus(Side side, HandlerData data, StorageMode mode, AutoMode autoMode, boolean enabled) {
        TechworksPacketHandler.syncHandlerStatus(machineTile.getPos(), data, side, mode, autoMode, enabled);
        machineTile.getMachineIO().setConfigStatus(data.getIdentity(), side, data.getType(), mode, autoMode, enabled);
    }

    public void changeMode(Side side, HandlerConfig config, StorageMode mode, AutoMode autoMode) {
        TechworksPacketHandler.syncHandlerConfigMode(machineTile.getPos(), mode, autoMode, config, side);
        machineTile.getMachineIO().setHandlerConfigMode(config.getBaseData().getIdentity(), side, mode, config.getBaseData().getType(), autoMode);
    }

    public void addData(HandlerData data) {
        if (!dataList.contains(data)) {
            dataList.add(data);
        }
    }

    public List<HandlerData> getDataList() {
        return dataList;
    }

    public T getMachineTile() {
        return machineTile;
    }

    public ComponentsWidget getComponentsWidget() {
        return componentsWidget;
    }
}
