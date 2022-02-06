package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.component.*;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class TechworksComponents {
    public static final DeferredRegister<ComponentType<?>> TYPES = DeferredRegister.create(Utils.<ComponentType<?>>cast(ComponentType.class), Techworks.MOD_ID);

    public static final RegistryObject<SmeltingComponent.Type> SMELTING = TYPES.register("smelting", SmeltingComponent.Type::new);
    public static final RegistryObject<EnergyStorageComponent.Type> ENERGY_STORAGE = TYPES.register("energy_storage", EnergyStorageComponent.Type::new);
    public static final RegistryObject<LiquidStorageComponent.Type> LIQUID_STORAGE = TYPES.register("liquid_storage", LiquidStorageComponent.Type::new);
    public static final RegistryObject<GasStorageComponent.Type> GAS_STORAGE = TYPES.register("gas_storage", GasStorageComponent.Type::new);
    public static final RegistryObject<HeatingComponent.Type> HEATING = TYPES.register("heating", HeatingComponent.Type::new);
    public static final RegistryObject<GrindingComponent.Type> GRINDING = TYPES.register("grinding", GrindingComponent.Type::new);
    public static final RegistryObject<SteamTurbineComponent.Type> STEAM_TURBINE = TYPES.register("steam_turbine", SteamTurbineComponent.Type::new);
    public static final RegistryObject<MiningComponent.Type> MINING = TYPES.register("mining", MiningComponent.Type::new);
}
