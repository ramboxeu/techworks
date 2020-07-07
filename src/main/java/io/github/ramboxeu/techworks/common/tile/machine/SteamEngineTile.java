package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.api.gas.GasHandler;
import io.github.ramboxeu.techworks.api.gas.IGasHandler;
import io.github.ramboxeu.techworks.client.container.SteamEngineContainer;
import io.github.ramboxeu.techworks.common.capability.InventoryItemStackHandler;
import io.github.ramboxeu.techworks.common.capability.extensions.IInventoryItemStackHandler;
import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.PredicateUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SteamEngineTile extends BaseMachineTile {
    public SteamEngineTile() {
        super(Registration.STEAM_ENGINE_TILE.get());
    }

//    @Override
//    void run() {
//        this.gasHandler.ifPresent(gas -> {
//            this.energyStorage.ifPresent(energy -> {
//                if (gas.extractGas(Registration.STEAM_GAS.get(), 100, false) == 100) {
//                    energy.receiveEnergy(100, false);
//                }
//
//                List<IEnergyStorage> handlers = new ArrayList<>();
//                for (Direction direction : Direction.values()) {
//                    TileEntity te = world.getTileEntity(pos.offset(direction));
//                    if (te != null) {
//                        te.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(handlers::add);
//                    }
//                }
//
//                for (int i = handlers.size(); i > 0; i--) {
//                    handlers.get(i - 1).receiveEnergy(energy.extractEnergy(energy.getEnergyStored() / i, false), false);
//                }
//            });
//        });
//    }


    @Override
    public void tick() {

    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.techworks.steam_engine");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity entity) {
        return new SteamEngineContainer(id, inventory, this);
    }
}