package io.github.ramboxeu.techworks.common.heat;

public interface IConfiguringHeater extends IHeater {
    void configure(IHeaterConfigurationStore config);
}
