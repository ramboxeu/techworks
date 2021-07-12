package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.client.screen.widget.config.ComponentsWidget;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.Side;
import io.github.ramboxeu.techworks.common.util.inventory.SlotBuilder;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseMachineContainer<T extends BaseMachineTile> extends BaseContainer {
    protected IItemHandler playerInventory;
    protected T machineTile;
    private int firstPlayerSlot = 0;

    protected final List<HandlerData> dataList;
    protected final Map<Side, List<HandlerConfig>> dataMap;
    protected final ComponentsWidget componentsWidget;

    public BaseMachineContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, T machineTile, @Deprecated Map<Side, List<HandlerConfig>> dataMap) {
        super(containerType, id);
        this.dataMap = dataMap;

        dataList = new ArrayList<>();

        this.machineTile = machineTile;
        this.playerInventory = new InvWrapper(playerInventory);
        addPlayerListener(playerInventory.player);

        componentsWidget = addWidget(new ComponentsWidget(machineTile.getComponentStorage()));

        this.layoutPlayerSlots();
    }

    protected Slot addSlot(SlotBuilder slotBuilder) {
        return addSlot(slotBuilder.build());
    }

    private void layoutPlayerSlots() {
        firstPlayerSlot = inventorySlots.size();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new SlotItemHandler(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new SlotItemHandler(playerInventory, i, 8 + i *18, 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        Slot source = inventorySlots.get(index);

        if (source != null) {
            ItemStack sourceStack = source.getStack();

            if (index >= firstPlayerSlot && index < firstPlayerSlot + 36) {
                ItemStack stack = sourceStack.copy();

                for (int i = 0; i < inventorySlots.size(); i++) {
                    if (i >= firstPlayerSlot && i < firstPlayerSlot + 36) {
                        i = firstPlayerSlot + 36;
                    }

                    Slot target = inventorySlots.get(i);
                    ItemStack targetStack = target.getStack().copy();

                    if (!targetStack.isEmpty())  {
                        if (ItemStack.areItemStacksEqual(stack, targetStack)) {
                            int size = target.getItemStackLimit(stack) - targetStack.getCount();

                            if (stack.getCount() <= size) {
                                targetStack.grow(stack.getCount());
                                target.putStack(targetStack);
                                stack = ItemStack.EMPTY;
                                break;
                            } else {
                                targetStack.grow(size);
                                stack.shrink(size);
                                target.putStack(targetStack);
                            }
                        }
                    } else {
                        if (target.isItemValid(stack)) {
                            int size = target.getItemStackLimit(stack);

                            if (stack.getCount() <= size) {
                                target.putStack(stack);
                                stack = ItemStack.EMPTY;
                                break;
                            } else {
                                target.putStack(stack.split(size));
                            }
                        }
                    }
                }

                source.putStack(stack);
                return ItemStack.EMPTY;
            } else {
                if (!this.mergeItemStack(sourceStack, firstPlayerSlot, 36, false)) {
                    return ItemStack.EMPTY;
                }

                source.onSlotChanged();
            }
        }

        return ItemStack.EMPTY;
    }

    public void changeStatus(Side side, HandlerData data, StorageMode mode, boolean enabled) {
        TechworksPacketHandler.syncHandlerStatus(machineTile.getPos(), data, side, mode, enabled);
//        List<HandlerConfig> list = dataMap.computeIfAbsent(side, s -> new ArrayList<>());
        HandlerConfig config = machineTile.getMachineIO().setConfigStatus(data.getIdentity(), side, data.getType(), mode, enabled);

//        if (config != null) {
//            if (enabled) {
//                list.add(config);
//            } else {
//                list.remove(config);
//            }
//        }
    }

    public void changeMode(Side side, HandlerConfig config, StorageMode mode) {
        TechworksPacketHandler.syncHandlerConfigMode(machineTile.getPos(), mode, config, side);

        machineTile.getMachineIO().setHandlerConfigMode(config.getBaseData().getIdentity(), side, mode, config.getBaseData().getType());
//        dataMap.computeIfAbsent(side, s -> new ArrayList<>()).stream().filter(c -> c.getBaseData() == data).findFirst().ifPresent(c -> c.setMode(mode));
    }

    public void addData(HandlerData data) {
        if (!dataList.contains(data)) {
            dataList.add(data);
        }
    }

    public List<HandlerData> getDataList() {
        return dataList;
    }

    public Map<Side, List<HandlerConfig>> getDataMap() {
        return dataMap;
    }

    public T getMachineTile() {
        return machineTile;
    }

    public ComponentsWidget getComponentsWidget() {
        return componentsWidget;
    }
}
