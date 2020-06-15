package io.github.ramboxeu.techworks.common.container;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.blockentity.machine.AbstractMachineBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.DefaultedList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractMachineContainer<T extends AbstractMachineBlockEntity<T>> extends Container {
    protected T blockEntity;
    protected PlayerInventory playerInventory;
    protected List<IAutoSyncable> syncableList;
    protected List<Integer> syncedValues;

    public AbstractMachineContainer(int syncId, PlayerInventory playerInventory, T blockEntity) {
        super(null, syncId);
        this.blockEntity = blockEntity;
        this.playerInventory = playerInventory;

        syncableList = blockEntity.getComponentList().stream()
                .filter(component -> component instanceof IAutoSyncable)
                .map(component -> (IAutoSyncable) component)
                .collect(Collectors.toList());

        syncedValues = new ArrayList<>();

        this.addProperties(new PropertyDelegate() {
            @Override
            public int get(int index) {
                if (syncableList.size() > index) {
                    return syncableList.get(index).getValue();
                } else {
                    return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                if (syncedValues.size() > index) {
                    syncedValues.set(index, value);
                } else {
                    syncedValues.add(value);
                }
            }

            @Override
            public int size() {
                return syncableList.size() + 1;
            }
        });
    }

    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    protected int getSyncedValueOrDefault(int index, int defaultValue) {
        if (syncedValues.size() > index) {
            return syncedValues.get(index);
        } else {
            return defaultValue;
        }
    }
}
