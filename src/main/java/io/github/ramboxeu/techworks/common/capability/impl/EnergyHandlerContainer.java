package io.github.ramboxeu.techworks.common.capability.impl;

import io.github.ramboxeu.techworks.common.util.Utils;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import io.github.ramboxeu.techworks.common.util.machineio.config.EnergyHandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.handler.IHandlerContainer;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.*;

/*
 * This handler is not saved, nor it saves its children. This means handlers should be saved separately.
 */
public class EnergyHandlerContainer implements IEnergyStorage, IHandlerContainer {
    private final List<EnergyHandlerConfig> storages;

    public EnergyHandlerContainer() {
        storages = new ArrayList<>();
    }

    public void addHandlers(EnergyHandlerConfig... storages) {
        addHandlers(Arrays.asList(storages));
    }

    public void addHandlers(List<EnergyHandlerConfig> storages) {
        for (EnergyHandlerConfig storage : storages) {
            addHandler(storage);
        }
    }

    public EnergyHandlerConfig addHandler(EnergyHandlerConfig storage) {
        EnergyHandlerConfig existing = Utils.getExistingConfig(storages, storage.getData());

        if (existing != null) {
            return existing;
        }

        storages.add(storage);
        return storage;
    }

    @Override
    public void setStorageMode(HandlerData data, StorageMode mode) {
        storages.stream().filter(config -> config.getBaseData() == data).findFirst().ifPresent(config -> config.setMode(mode));
    }

    public List<HandlerConfig> getConfigs() {
        return Collections.unmodifiableList(storages);
    }

    @Override
    public HandlerConfig remove(HandlerData data) {
        return storages.stream().filter(config -> config.getBaseData() == data).findFirst().map(config -> { storages.remove(config); return config; }).orElse(null);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = 0;

        for (Iterator<EnergyHandlerConfig> it = storages.stream().filter(config -> config.getMode().canInput()).iterator(); it.hasNext(); ) {
            EnergyHandlerConfig storage = it.next();
            received += storage.getStorage().receiveEnergy(maxReceive - received, simulate);

            if (received == maxReceive) {
                break;
            }
        }

        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extracted = 0;

        for (Iterator<EnergyHandlerConfig> it = storages.stream().filter(config -> config.getMode().canOutput()).iterator(); it.hasNext(); ) {
            EnergyHandlerConfig storage = it.next();
            extracted += storage.getStorage().extractEnergy(maxExtract - extracted, simulate);

            if (extracted == maxExtract) {
                break;
            }
        }

        return extracted;
    }

    @Override
    public int getEnergyStored() {
        return storages.stream().mapToInt(config -> config.getStorage().getEnergyStored()).sum();
    }

    @Override
    public int getMaxEnergyStored() {
        return storages.stream().mapToInt(config -> config.getStorage().getMaxEnergyStored()).sum();
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
