package io.github.ramboxeu.techworks.common.tile.cable;

import io.github.ramboxeu.techworks.common.debug.DebugInfoBuilder;
import io.github.ramboxeu.techworks.common.registration.Registration;
import net.minecraft.util.Direction;

import java.util.List;

public class BasicGasPipeTile extends AbstractCableTile<Object> {
    private int maxTransfer;
    private int transferred = 0;

    public BasicGasPipeTile() {
        super(Registration.GAS_PIPE_BASIC_TILE.get(), null);
    }

    @Override
    protected Object createHandler() {
        return null;
    }

    @Override
    protected boolean transfer(Object pipe, List<Object> transferees) {
        return false;
    }

    @Override
    protected Object createCapability(Direction side, Object o, List<Direction> inputs) {
        return null;
    }

    @Override
    protected boolean handlerHasContents() {
        return false;
    }

    @Override
    public void addDebugInfo(DebugInfoBuilder builder) {
        super.addDebugInfo(builder);

        builder.addSection(new DebugInfoBuilder.Section("Transfer").line("Max: " + maxTransfer).line("Transferred: " + transferred));
    }
}
