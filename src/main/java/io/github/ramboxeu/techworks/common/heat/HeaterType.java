package io.github.ramboxeu.techworks.common.heat;

import java.util.function.Supplier;

public enum HeaterType {
    SOLID_FUEL(SolidFuelHeater::new),
    ELECTRIC(() -> null),
    BIO_FUEL(() -> null);

    private final Supplier<IHeater> factory;

    HeaterType(Supplier<IHeater> factory) {
        this.factory = factory;
    }

    public IHeater createHeater() {
        return factory.get();
    }
}
