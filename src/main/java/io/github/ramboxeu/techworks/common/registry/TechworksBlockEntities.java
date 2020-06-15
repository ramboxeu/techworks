package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.blockentity.machine.BoilerBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class TechworksBlockEntities {
    public static BlockEntityType<BoilerBlockEntity> BOILER;

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, Supplier<T> blockEntity, Block... validBlocks) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Techworks.MOD_ID, name), BlockEntityType.Builder.create(blockEntity, validBlocks).build(null));
    }

    public static void registerAll() {
        BOILER = register("boiler", BoilerBlockEntity::new, TechworksBlocks.BOILER);
    }
}
