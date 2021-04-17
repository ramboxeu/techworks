package io.github.ramboxeu.techworks.client;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.DataConstants;
import io.github.ramboxeu.techworks.common.block.BaseMachineBlock;
import io.github.ramboxeu.techworks.common.property.TechworksBlockStateProperties;
import io.github.ramboxeu.techworks.common.registration.TechworksBlocks;
import io.github.ramboxeu.techworks.common.registry.BlockRegistryObject;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
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
        Techworks.LOGGER.debug("Generating...");

        for (BlockRegistryObject<? extends BaseMachineBlock, BlockItem> machine : TechworksBlocks.MACHINES) {
            machineBlock(machine.getBlock(), machine.getId().getPath());
            machineBlockItem(machine.getId().getPath());
        }

        horizontalBlock(TechworksBlocks.BLUEPRINT_TABLE);

        horizontalBlock(
                TechworksBlocks.ASSEMBLY_TABLE.getBlock(),
                machineBlockModel("assembly_table", modLoc("block/assembly_table_front"))
        );
        blockItem("assembly_table");

        blockAndItem(TechworksBlocks.DEV_BLOCK);

        Arrays.stream(DataConstants.Blocks.CABLES).forEach(this::cableBlockAndItem);
    }

    private void cableBlockAndItem(BlockRegistryObject<?, ?> cable) {
        String name = cable.getId().getPath();

        getVariantBuilder(cable.getBlock()).partialState()
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

    private void machineBlock(Block machineBlock, String name) {
        ResourceLocation frontOffTex = modLoc("block/" + name + "_front_off");
        ResourceLocation frontOnTex = modLoc("block/" + name + "_front_on");

        BlockModelBuilder offModel = machineBlockModel(name + "_off", frontOffTex);
        BlockModelBuilder onModel = machineBlockModel(name + "_on", frontOnTex);

        getVariantBuilder(machineBlock).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(state.get(TechworksBlockStateProperties.RUNNING) ? onModel : offModel)
                .rotationY(getHorizontalRotation(state.get(BlockStateProperties.HORIZONTAL_FACING)))
                .build()
        );
    }

    private void machineBlockItem(String name) {
        models().withExistingParent("item/" + name, modLoc("block/" + name + "_off"));
    }

    private BlockModelBuilder machineBlockModel(String name, ResourceLocation frontTex) {
        return models().cube(name,
                DataConstants.Textures.MACHINE_BOTTOM,
                DataConstants.Textures.MACHINE_TOP,
                frontTex,
                DataConstants.Textures.MACHINE_SIDE,
                DataConstants.Textures.MACHINE_SIDE,
                DataConstants.Textures.MACHINE_SIDE
        ).texture("particle", DataConstants.Textures.MACHINE_SIDE);
    }

    private void horizontalBlock(BlockRegistryObject<?, ?> object) {
        String name = object.getId().getPath();
        horizontalBlock(object.getBlock(), new ModelFile.ExistingModelFile(modLoc("block/" + name), helper));
        blockItem(name);
    }

    private void blockAndItem(BlockRegistryObject<?, ?> block) {
        simpleBlock(block);
        blockItem(block.getId().getPath());
    }

    private void simpleBlock(BlockRegistryObject<?, ?> block) {
        simpleBlock(block.getBlock());
    }

    private void blockItem(String name) {
        models().withExistingParent("item/" + name, modLoc("block/" + name));
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
