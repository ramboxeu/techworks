package io.github.ramboxeu.techworks.common.gas;

import net.minecraftforge.registries.ForgeRegistryEntry;

public class Gas extends ForgeRegistryEntry<Gas> {
    public class Unit {
        private Gas gas;
        private int pressure;
        private int amount;

        public Unit(Gas gas, int pressure, int amount) {
            this.gas = gas;
            this.pressure = pressure;
            this.amount = amount;
        }

        public Gas getGas() {
            return gas;
        }

        public int getPressure(){
            return pressure;
        }

        public int getAmount() {
            return amount;
        }
    }
}
