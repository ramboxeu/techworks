package io.github.ramboxeu.techworks.common.tile.cable;

import io.github.ramboxeu.techworks.api.gas.CapabilityGas;
import io.github.ramboxeu.techworks.api.gas.GasHandler;
import io.github.ramboxeu.techworks.api.gas.IGasHandler;
import io.github.ramboxeu.techworks.common.debug.DebugInfoBuilder;
import io.github.ramboxeu.techworks.common.gas.Gas;
import io.github.ramboxeu.techworks.common.registration.Registration;
import net.minecraft.util.Direction;

import java.util.List;

public class BasicGasPipeTile extends AbstractCableTile<IGasHandler> {
    private int maxTransfer;
    private int transferred = 0;

    public BasicGasPipeTile() {
        super(Registration.GAS_PIPE_BASIC_TILE.get(), CapabilityGas.GAS);
    }

    @Override
    protected IGasHandler createHandler() {
        return new GasHandler(Registration.STEAM_GAS.get(), 400, 400){
            @Override
            public void onContentsChanged() {
                markDirty();
            }
        };
    }

    @Override
    protected IGasHandler createCapability(Direction side, IGasHandler iGasHandler, List<Direction> inputs) {
        return new GasHandler(Registration.STEAM_GAS.get(), 400, 400) {
            @Override
            public int insertGas(Gas gas, int amount, boolean simulate) {
                if (!simulate) {
                    if (!inputs.contains(side)) {
                        inputs.add(side);
                    }
                    inserted = true;
                }
                return iGasHandler.insertGas(gas, amount, simulate);
            }

            @Override
            public int extractGas(Gas gas, int amount, boolean simulate) {
                return iGasHandler.extractGas(gas, amount, simulate);
            }

            @Override
            public int getAmountStored() {
                return iGasHandler.getAmountStored();
            }
        };
    }

    @Override
    protected boolean handlerHasContents() {
        return handler.orElse(new GasHandler()).getAmountStored() > 0;
    }

    @Override
    protected boolean transfer(IGasHandler pipe, List<IGasHandler> transferees) {
        int validTransferees = 0;

        for (IGasHandler handler : transferees) {
            if (handler.getAmountStored() != handler.getMaxStorage()) {
                validTransferees += 1;
            }
        }

        if (validTransferees > 0) {
            maxTransfer = pipe.getAmountStored() / validTransferees;

            for (IGasHandler handler : transferees) {
                transferred = handler.insertGas(Registration.STEAM_GAS.get(), pipe.extractGas(Registration.STEAM_GAS.get(), maxTransfer, false), false);
                maxTransfer = pipe.getAmountStored() / validTransferees;
                //validTransferees -= 1;
            }
        }

        return true;
    }

    @Override
    public void addDebugInfo(DebugInfoBuilder builder) {
        super.addDebugInfo(builder);

        builder.addSection(new DebugInfoBuilder.Section("GasHandler").line(Integer.toString(this.handler.orElse(new GasHandler()).getAmountStored())));
        builder.addSection(new DebugInfoBuilder.Section("Transfer").line("Max: " + maxTransfer).line("Transferred: " + transferred));
    }
}
