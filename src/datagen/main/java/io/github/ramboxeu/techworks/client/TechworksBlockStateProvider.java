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
import net.minecraftforge.client.model.generators.*;

public class TechworksBlockStateProvider extends BlockStateProvider {
    private final ExistingFileHelper helper;

    public TechworksBlockStateProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Techworks.MOD_ID, helper);

        this.helper = helper;
    }

    @Override
    protected void registerStatesAndModels() {
        for (BlockRegistryObject<? extends BaseMachineBlock, BlockItem> machine : TechworksBlocks.MACHINES) {
            machineBlock(machine.getBlock(), machine.getRegistryName().getPath());
            machineBlockItem(machine.getRegistryName().getPath());
        }

        horizontalBlock(TechworksBlocks.BLUEPRINT_TABLE);
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

    private void horizontalBlock(BlockRegistryObject<?, ?> object) {
        String name = object.getRegistryName().getPath();
        horizontalBlock(object.getBlock(), new ModelFile.ExistingModelFile(modLoc("block/" + name), helper));
        blockItem(name);
    }

    private void blockItem(String name) {
        models().withExistingParent("item/" + name, modLoc("block/" + name));
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
