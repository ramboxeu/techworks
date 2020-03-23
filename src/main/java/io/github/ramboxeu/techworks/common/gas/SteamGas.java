package io.github.ramboxeu.techworks.common.gas;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.gas.Gas;
import net.minecraft.util.ResourceLocation;

public class SteamGas extends Gas {
    public SteamGas() {
        super(new ResourceLocation(Techworks.MOD_ID, "textures/gas/steam.png"), "Steam");
    }
}
