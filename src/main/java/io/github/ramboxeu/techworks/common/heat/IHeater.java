package io.github.ramboxeu.techworks.common.heat;

public interface IHeater {
    void tick();
    int extractHeat();
    HeaterType getType();
}
