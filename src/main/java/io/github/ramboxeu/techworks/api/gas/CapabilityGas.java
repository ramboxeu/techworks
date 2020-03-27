package io.github.ramboxeu.techworks.api.gas;

import io.github.ramboxeu.techworks.common.registration.Registration;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityGas {
    @CapabilityInject(IGasHandler.class)
    public static Capability<IGasHandler> GAS = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IGasHandler.class, new Capability.IStorage<IGasHandler>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IGasHandler> capability, IGasHandler instance, Direction side) {
                CompoundNBT nbt = new CompoundNBT();
                nbt.putString("Gas", instance.getGas().getRegistryName().toString());
                nbt.putInt("Amount", instance.getAmountStored());

                return nbt;
            }

            @Override
            public void readNBT(Capability<IGasHandler> capability, IGasHandler instance, Direction side, INBT nbt) {
                if (!(instance instanceof GasHandler)) {
                    throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
                }
                CompoundNBT compound = (CompoundNBT)nbt;
                GasHandler handler = (GasHandler)instance;
                handler.amountStored = compound.getInt("Amount");
                handler.gas = Registration.getGasByString(compound.getString("Gas"));
            }
        }, GasHandler::new);
    }
}
