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
import net.minecraftforge.client.model.generators.ExistingFileHelper;

public class TechworksBlockStateProvider extends BlockStateProvider {
    public TechworksBlockStateProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Techworks.MOD_ID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (BlockRegistryObject<? extends BaseMachineBlock, BlockItem> machine : TechworksBlocks.MACHINES) {
            machineBlock(machine.getBlock(), machine.getRegistryName().getPath());
            machineBlockItem(machine.getRegistryName().getPath());
        }
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
