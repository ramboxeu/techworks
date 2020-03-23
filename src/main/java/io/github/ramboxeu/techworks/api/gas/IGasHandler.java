package io.github.ramboxeu.techworks.api.gas;

import net.minecraft.util.Direction;

public interface IGasHandler {
    /*
        Returns whatever connection can be made on given side
    */
    boolean canConnect(Direction side);

    /*
        Returns what gas given side handles
    */
    Gas getGas(Direction side);

    /*
        Extracts gas from handler
        @return amount of gas that was extracted or would have been if simulated
    */
    int extractGas(Direction side, Gas gas, int amount, boolean simulate);

    /*
        Inserts gas to handler
        @return amount of gas that was inserted or would have been if simulated
    */
    int insertGas(Direction side, Gas gas, int amount, boolean simulate);

    /*
        Gets maximum capacity of handler for given side
     */
    int getMaxStorage(Direction side);

    /*
        Gets amount of gas stored in handler for given side
    */
    int getStorage(Direction side);
}
