package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.component.ComponentType;
import io.github.ramboxeu.techworks.common.component.EnergyStorageComponent;
import io.github.ramboxeu.techworks.common.component.SmeltingComponent;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class TechworksComponents {
    public static final DeferredRegister<ComponentType<?>> TYPES = DeferredRegister.create(Utils.<ComponentType<?>>cast(ComponentType.class), Techworks.MOD_ID);

    public static final RegistryObject<SmeltingComponent.Type> SMELTING = TYPES.register("smelting", SmeltingComponent.Type::new);
    public static final RegistryObject<EnergyStorageComponent.Type> ENERGY_STORAGE = TYPES.register("energy_storage", EnergyStorageComponent.Type::new);
}
