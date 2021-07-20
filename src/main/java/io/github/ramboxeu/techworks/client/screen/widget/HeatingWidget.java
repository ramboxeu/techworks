package io.github.ramboxeu.techworks.client.screen.widget;

import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.common.heat.IHeater;
import io.github.ramboxeu.techworks.common.heat.SolidFuelHeater;

public class HeatingWidget extends BaseContainerWidget {
    private final IHeater heater;
    private final int solidFuelBurnerX;
    private final int solidFuelBurnerY;
    private final int electricHeaterX;
    private final int electricHeaterY;
    private final int bioFuelBurnerX;
    private final int bioFuelBurnerY;

    public HeatingWidget(IHeater heater, int solidFuelBurnerX, int solidFuelBurnerY, int electricHeaterX, int electricHeaterY, int bioFuelBurnerX, int bioFuelBurnerY) {
        this.heater = heater;
        this.solidFuelBurnerX = solidFuelBurnerX;
        this.solidFuelBurnerY = solidFuelBurnerY;
        this.electricHeaterX = electricHeaterX;
        this.electricHeaterY = electricHeaterY;
        this.bioFuelBurnerX = bioFuelBurnerX;
        this.bioFuelBurnerY = bioFuelBurnerY;
    }

    @Override
    public void init(BaseContainer container, Builder builder) {
        switch (heater.getType()) {
            case SOLID_FUEL:
                builder.subWidget(new SolidFuelHeatingWidget((SolidFuelHeater) heater, solidFuelBurnerX, solidFuelBurnerY));
        }
    }
}
