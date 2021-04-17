package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.DevBlockTile;
import io.github.ramboxeu.techworks.common.util.Side;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;
import java.util.List;

public class DevBlockContainer extends BaseContainer {
    private final DevBlockTile tile;

    public DevBlockContainer(int id, DevBlockTile tile) {
        super(TechworksContainers.DEV_BLOCK.get(), id);
        this.tile = tile;
    }

    public DevBlockTile getTile() {
        return tile;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    public void syncEnergy(int energy, DevBlockTile.ActiveSignal signal, EnumSet<Side> sides) {
        TechworksPacketHandler.syncDevEnergy(tile.getPos(), energy, signal, sides);
        tile.configureEnergy(energy, sides, signal);
    }

    public void syncLiquid(int liquidPerTick, Fluid liquid, DevBlockTile.ActiveSignal signal, EnumSet<Side> sides) {
        TechworksPacketHandler.syncDevLiquid(tile.getPos(), liquidPerTick, liquid, sides, signal);
        tile.configureLiquid(liquidPerTick, liquid, sides, signal);
    }

    public void syncGas(int gasPerTick, Fluid gas, DevBlockTile.ActiveSignal signal, EnumSet<Side> sides) {
        TechworksPacketHandler.syncDevGas(tile.getPos(), gasPerTick, gas, sides, signal);
        tile.configureGas(gasPerTick, gas, sides, signal);
    }

    public void syncInv(List<ItemStack> inv, DevBlockTile.ActiveSignal signal, EnumSet<Side> sides) {
        TechworksPacketHandler.syncDevItem(tile.getPos(), inv, sides, signal);
        tile.configureItem(inv, sides, signal);
    }
}
