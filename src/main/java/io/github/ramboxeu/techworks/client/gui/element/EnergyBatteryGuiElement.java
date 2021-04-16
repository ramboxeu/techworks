package io.github.ramboxeu.techworks.client.gui.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.util.machineio.data.EnergyHandlerData;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.Collections;
import java.util.List;

public class EnergyBatteryGuiElement extends PortGuiElement implements IIntTracker {
    private int energy;
    private final IEnergyStorage storage;

    public EnergyBatteryGuiElement(int x, int y, EnergyHandlerData data) {
        super(x, y, 0, 0, data.getColor());

        this.storage = data.getHandler();
    }

    @Override
    public List<IntReferenceHolder> getIntHolders() {
        return Collections.singletonList(new IntReferenceHolder() {
            @Override
            public int get() {
                return storage.getEnergyStored();
            }

            @Override
            public void set(int value) {
                energy = value;
            }
        });
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Techworks.LOGGER.debug("energy = {}, color = {}", energy, color);
    }
}
