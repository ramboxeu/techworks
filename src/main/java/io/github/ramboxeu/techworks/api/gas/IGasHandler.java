package io.github.ramboxeu.techworks.api.gas;

import io.github.ramboxeu.techworks.common.gas.Gas;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IGasHandler extends INBTSerializable<CompoundNBT> {
    /*
        Returns what gas capability handles
        @return gas handled
    */
    Gas getGas();

    int insertGas(Gas gas, int amount, boolean simulate);

    int extractGas(Gas gas, int amount, boolean simulate);

    /*
        Gets maximum capacity of handler
        @return max amount of cas that can be stored
     */
    int getMaxStorage();

    /*
        Gets amount of gas stored in handler
        @return amount of gas stored
    */
    int getAmountStored();

    boolean canExtract();
    boolean canInsert();
}
