package io.github.ramboxeu.techworks.api.gas;

public interface IGasHandler {
    /*
        Returns what gas given side handles
    */
    Gas getGas();

    /*
        Extracts gas from handler
        @return amount of gas that was extracted or would have been if simulated
    */
    int extractGas(Gas gas, int amount, boolean simulate);

    /*
        Inserts gas to handler
        @return amount of gas that was inserted or would have been if simulated
    */
    int insertGas(Gas gas, int amount, boolean simulate);

    /*
        Gets maximum capacity of handler for given side
     */
    int getMaxStorage();

    /*
        Gets amount of gas stored in handler for given side
    */
    int getStorage();

    boolean canExtract();
    boolean canInsert();
}
