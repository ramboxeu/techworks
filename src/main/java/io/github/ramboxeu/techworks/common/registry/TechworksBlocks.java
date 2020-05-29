package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.block.machine.BoilerBlock;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TechworksBlocks {
    public static BoilerBlock BOILER;

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(Registry.BLOCK, new Identifier(Techworks.MOD_ID, name), block);
    }

    // Full static (no method calls) would be cool, but it's seems like it's getting optimized away
    public static void registerAll() {
        BOILER = register("boiler", new BoilerBlock());
    }
}
