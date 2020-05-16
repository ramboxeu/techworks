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
                inputs.add(side);
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
    protected boolean transfer(IGasHandler pipe, List<IGasHandler> transferees) {
        int maxTransfer = pipe.getAmountStored() / transferees.size();

        for (int i = 0; i < transferees.size(); i++) {
            int transferred = transferees.get(i).insertGas(Registration.STEAM_GAS.get(), pipe.extractGas(Registration.STEAM_GAS.get(), maxTransfer, false), false);
            maxTransfer = pipe.getAmountStored() / transferees.size() - i + 1;
        }

        return true;
    }

    @Override
    public void addDebugInfo(DebugInfoBuilder builder) {
        super.addDebugInfo(builder);

        builder.addSection(new DebugInfoBuilder.Section("GasHandler").line(Integer.toString(this.handler.orElse(new GasHandler()).getAmountStored())));
    }
}
