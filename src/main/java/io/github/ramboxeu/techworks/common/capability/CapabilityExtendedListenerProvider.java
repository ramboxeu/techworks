package io.github.ramboxeu.techworks.common.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityExtendedListenerProvider {
    @CapabilityInject(IExtendedListenerProvider.class)
    public static Capability<IExtendedListenerProvider> EXTENDED_LISTENER_PROVIDER = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IExtendedListenerProvider.class, new Capability.IStorage<IExtendedListenerProvider>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IExtendedListenerProvider> capability, IExtendedListenerProvider instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<IExtendedListenerProvider> capability, IExtendedListenerProvider instance, Direction side, INBT nbt) {}
        }, ExtendedListenerProvider::new);
    }
}
