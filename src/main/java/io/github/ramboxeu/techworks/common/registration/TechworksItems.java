package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.base.BaseBoilingComponent;
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
    public static final RegistryObject<BaseBoilingComponent> BASE_BOILING_COMPONENT = ITEMS.register("base_boiling_component", BaseBoilingComponent::new);
    public static final RegistryObject<BaseBoilingComponent> BASIC_BOILING_COMPONENT = ITEMS.register("basic_boiling_component", () -> new BaseBoilingComponent(1, 500));
    public static final RegistryObject<BaseBoilingComponent> ADVANCED_BOILING_COMPONENT = ITEMS.register("advanced_boiling_component", () -> new BaseBoilingComponent(2, 1000));

    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench",
            () -> new Item(new Item.Properties().maxStackSize(1).group(Techworks.ITEM_GROUP)));
}
