package io.github.ramboxeu.techworks.client.lang;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.component.ComponentType;
import io.github.ramboxeu.techworks.common.lang.TranslationKey;
import io.github.ramboxeu.techworks.common.registry.BlockRegistryObject;
import io.github.ramboxeu.techworks.common.registry.FluidRegistryObject;
import io.github.ramboxeu.techworks.common.registry.ItemRegistryObject;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.RegistryObject;

public abstract class BaseLangProvider extends LanguageProvider {
    protected final String locale;

    public BaseLangProvider(DataGenerator gen, String locale) {
        super(gen, Techworks.MOD_ID, locale);
        this.locale = locale;
    }

    protected void add(BlockRegistryObject<?, ?> block, String name) {
        add(block.get().getTranslationKey(), name);
    }

    protected void add(ItemRegistryObject<?> item, String name) {
        add(item.get().getTranslationKey(), name);
    }

    protected void add(TranslationKey key, String name) {
        add(key.get(), name);
    }

    protected void add(FluidRegistryObject<?> fluid, String name) {
        add(fluid.get().getAttributes().getTranslationKey(), name);
    }

    protected void add(RegistryObject<? extends ComponentType<?>> type, String name) {
        add(type.get().getTranslationKey(), name);
    }

    @Override
    public String getName() {
        return "Languages: techworks: " + locale;
    }
}
