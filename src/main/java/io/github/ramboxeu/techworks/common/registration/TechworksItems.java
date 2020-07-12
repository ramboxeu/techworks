package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.base.BaseBoilingComponent;
import io.github.ramboxeu.techworks.api.component.base.BaseGasStorageComponent;
import io.github.ramboxeu.techworks.api.component.base.BaseLiquidStorageComponent;
import io.github.ramboxeu.techworks.common.item.WrenchItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TechworksItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Techworks.MOD_ID);

    public static void addToEventBus() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Components
    public static final RegistryObject<BaseBoilingComponent> BASIC_BOILING_COMPONENT = ITEMS.register("basic_boiling_component", () -> new BaseBoilingComponent(1, 500));
    public static final RegistryObject<BaseBoilingComponent> ADVANCED_BOILING_COMPONENT = ITEMS.register("advanced_boiling_component", () -> new BaseBoilingComponent(2, 1000));

    public static final RegistryObject<BaseLiquidStorageComponent> BASIC_LIQUID_STORAGE_COMPONENT = ITEMS.register("basic_liquid_storage_component", () -> new BaseLiquidStorageComponent(1, 5000));
    public static final RegistryObject<BaseLiquidStorageComponent> ADVANCED_LIQUID_STORAGE_COMPONENT = ITEMS.register("advanced_liquid_storage_component", () -> new BaseLiquidStorageComponent(2, 10000));

    public static final RegistryObject<BaseGasStorageComponent> BASIC_GAS_STORAGE_COMPONENT = ITEMS.register("basic_gas_storage_component", () -> new BaseGasStorageComponent(1, 5000));
    public static final RegistryObject<BaseGasStorageComponent> ADVANCED_GAS_STORAGE_COMPONENT = ITEMS.register("advanced_gas_storage_component", () -> new BaseGasStorageComponent(2, 10000));

    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", WrenchItem::new);
}
