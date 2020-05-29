package io.github.ramboxeu.fabricworks.common.registry;

import io.github.ramboxeu.fabricworks.common.block.BoilerBlock;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TechworksBlocks {
    public static BoilerBlock BOILER;

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(Registry.BLOCK, new Identifier(name), block);
    }

    // Full static (no method calls) would be cool, but it's seems like it's getting optimized away
    public static void registerAll() {
        BOILER = register("boiler", new BoilerBlock());
    }
}
