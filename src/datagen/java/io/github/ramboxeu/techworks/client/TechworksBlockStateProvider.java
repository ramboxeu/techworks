package io.github.ramboxeu.techworks.client;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.DataConstants;
import io.github.ramboxeu.techworks.common.property.TechworksBlockStateProperties;
import io.github.ramboxeu.techworks.common.registration.TechworksBlocks;
import io.github.ramboxeu.techworks.common.registry.BlockRegistryObject;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;

public class TechworksBlockStateProvider extends BlockStateProvider {
    private final ExistingFileHelper helper;

    public TechworksBlockStateProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Techworks.MOD_ID, helper);

        this.helper = helper;
    }

    @Override
    protected void registerStatesAndModels() {
        for (BlockRegistryObject<?, ?> machine : DataConstants.Blocks.MACHINES) {
            machineBlock(machine.get(), machine.getId().getPath());
            directionalProcessingBlockItem(machine.getId().getPath());
        }

        directionalProcessingBlock(TechworksBlocks.SOLID_FUEL_BURNER.get(), "solid_fuel_burner", modLoc("block/solid_fuel_burner_side"), modLoc("block/machine_bottom"), modLoc("block/machine_top"));
        directionalProcessingBlockItem("solid_fuel_burner");

        horizontalBlock(TechworksBlocks.BLUEPRINT_TABLE);

        horizontalBlock(
                TechworksBlocks.ASSEMBLY_TABLE.get(),
                directionalProcessingBlockModel("assembly_table", modLoc("block/assembly_table_front"), DataConstants.Textures.MACHINE_SIDE, DataConstants.Textures.MACHINE_BOTTOM, DataConstants.Textures.MACHINE_TOP)
        );
        blockItem("assembly_table");

        blockAndItem(TechworksBlocks.DEV_BLOCK);

        Arrays.stream(DataConstants.Blocks.CABLES).forEach(this::cableBlockAndItem);
        Arrays.stream(DataConstants.Blocks.CUBE_ALL).forEach(this::blockAndItem);

        storageBlock(TechworksBlocks.LIQUID_TANK);
        storageBlock(TechworksBlocks.GAS_TANK);
        storageBlock(TechworksBlocks.ENERGY_STORAGE);
    }

    private void cableBlockAndItem(BlockRegistryObject<?, ?> cable) {
        String name = cable.getId().getPath();

        getVariantBuilder(cable.get()).partialState()
                .addModels(ConfiguredModel.builder()
                        .modelFile(cableBlockModel("block/" + name))
                        .build()
                );

        blockItem(name);
    }

    private BlockModelBuilder cableBlockModel(String name) {
        return models().getBuilder(modLoc(name).toString())
                .customLoader(CableModelBuilder::new)
                .particle(name)
                .base(name)
                .connector(name + "_connector")
                .end();
    }

    private void machineBlock(Block block, String name) {
        directionalProcessingBlock(block, name, DataConstants.Textures.MACHINE_SIDE, DataConstants.Textures.MACHINE_BOTTOM, DataConstants.Textures.MACHINE_TOP);
    }

    private void directionalProcessingBlock(Block block, String name, ResourceLocation sideTex, ResourceLocation bottomTex, ResourceLocation topTex) {
        ResourceLocation frontOffTex = modLoc("block/" + name + "_front_off");
        ResourceLocation frontOnTex = modLoc("block/" + name + "_front_on");

        BlockModelBuilder offModel = directionalProcessingBlockModel(name + "_off", frontOffTex, sideTex, bottomTex, topTex);
        BlockModelBuilder onModel = directionalProcessingBlockModel(name + "_on", frontOnTex, sideTex, bottomTex, topTex);

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(state.get(TechworksBlockStateProperties.RUNNING) ? onModel : offModel)
                .rotationY(getHorizontalRotation(state.get(BlockStateProperties.HORIZONTAL_FACING)))
                .build()
        );
    }

    private void directionalProcessingBlockItem(String name) {
        models().withExistingParent("item/" + name, modLoc("block/" + name + "_off"));
    }

    private BlockModelBuilder directionalProcessingBlockModel(String name, ResourceLocation frontTex, ResourceLocation sideTex, ResourceLocation bottomTex, ResourceLocation topTex) {
        return models().cube(name,
                bottomTex,
                topTex,
                frontTex,
                sideTex,
                sideTex,
                sideTex
        ).texture("particle", DataConstants.Textures.MACHINE_SIDE);
    }

    private void horizontalBlock(BlockRegistryObject<?, ?> object) {
        String name = object.getId().getPath();
        horizontalBlock(object.get(), new ModelFile.ExistingModelFile(modLoc("block/" + name), helper));
        blockItem(name);
    }

    private void blockAndItem(BlockRegistryObject<?, ?> block) {
        simpleBlock(block);
        blockItem(block.getId().getPath());
    }

    private void simpleBlock(BlockRegistryObject<?, ?> block) {
        simpleBlock(block.get());
    }

    private void blockItem(String name) {
        models().withExistingParent("item/" + name, modLoc("block/" + name));
    }

    private void storageBlock(BlockRegistryObject<?, ?> block) {
        String name = block.getId().getPath();
        simpleBlock(block.get(), new ModelFile.ExistingModelFile(modLoc("block/" + name), helper));
        models().withExistingParent("item/" + name, modLoc("item/storage_block"));
    }

    private int getHorizontalRotation(Direction facing) {
        switch (facing) {
            case SOUTH:
                return 180;
            case WEST:
                return 270;
            case EAST:
                return 90;
            default:
                return 0;
        }
    }
}
