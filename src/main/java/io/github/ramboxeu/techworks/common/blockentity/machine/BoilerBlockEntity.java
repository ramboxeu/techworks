package io.github.ramboxeu.techworks.common.blockentity.machine;

import io.github.ramboxeu.techworks.common.container.machine.BoilerContainer;
import io.github.ramboxeu.techworks.common.registry.TechworksBlockEntities;

public class BoilerBlockEntity extends AbstractMachineBlockEntity<BoilerBlockEntity, BoilerContainer> {
    public BoilerBlockEntity() {
        super(TechworksBlockEntities.BOILER);
    }
}
