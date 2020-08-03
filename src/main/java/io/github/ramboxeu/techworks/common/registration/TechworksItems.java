package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.api.component.base.BaseBoilingComponent;
import io.github.ramboxeu.techworks.api.component.base.BaseGasStorageComponent;
import io.github.ramboxeu.techworks.api.component.base.BaseLiquidStorageComponent;
import io.github.ramboxeu.techworks.common.item.GroundItem;
import io.github.ramboxeu.techworks.common.item.WrenchItem;
import io.github.ramboxeu.techworks.common.registry.ItemDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.ItemRegistryObject;

public class TechworksItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister();

    // Components
    public static final ItemRegistryObject<BaseBoilingComponent> BASIC_BOILING_COMPONENT = ITEMS.register("basic_boiling_component", () -> new BaseBoilingComponent(1, 500, 100, 300, 30));
    public static final ItemRegistryObject<BaseBoilingComponent> ADVANCED_BOILING_COMPONENT = ITEMS.register("advanced_boiling_component", () -> new BaseBoilingComponent(2, 1000, 50, 2000, 200));

    public static final ItemRegistryObject<BaseLiquidStorageComponent> BASIC_LIQUID_STORAGE_COMPONENT = ITEMS.register("basic_liquid_storage_component", () -> new BaseLiquidStorageComponent(1, 5000));
    public static final ItemRegistryObject<BaseLiquidStorageComponent> ADVANCED_LIQUID_STORAGE_COMPONENT = ITEMS.register("advanced_liquid_storage_component", () -> new BaseLiquidStorageComponent(2, 10000));

    public static final ItemRegistryObject<BaseGasStorageComponent> BASIC_GAS_STORAGE_COMPONENT = ITEMS.register("basic_gas_storage_component", () -> new BaseGasStorageComponent(1, 5000));
    public static final ItemRegistryObject<BaseGasStorageComponent> ADVANCED_GAS_STORAGE_COMPONENT = ITEMS.register("advanced_gas_storage_component", () -> new BaseGasStorageComponent(2, 10000));

    public static final ItemRegistryObject<WrenchItem> WRENCH = ITEMS.register("wrench", WrenchItem::new);

    public static final ItemRegistryObject<GroundItem> GROUND_IRON = ITEMS.register("ground_iron", GroundItem::new);
    public static final ItemRegistryObject<GroundItem> GROUND_GOLD = ITEMS.register("ground_gold", GroundItem::new);
}
