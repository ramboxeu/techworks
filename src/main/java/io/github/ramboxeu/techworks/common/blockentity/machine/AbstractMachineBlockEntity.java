package io.github.ramboxeu.techworks.common.blockentity.machine;

import io.github.ramboxeu.techworks.common.api.component.ComponentInventory;
import io.github.ramboxeu.techworks.common.registry.TechworksContainers;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.NameableContainerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Tickable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractMachineBlockEntity<TEntity extends AbstractMachineBlockEntity<TEntity>> extends BlockEntity implements NameableContainerFactory, Tickable {
    protected ComponentInventory<TEntity> componentList;

    public AbstractMachineBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    @Override
    public void tick() {
        componentList.tick();
    }

    public ComponentInventory<TEntity> getComponentList() {
        return componentList;
    }

    public ActionResult onActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ContainerProviderRegistry.INSTANCE.openContainer(TechworksContainers.BOILER, player, buf -> {
            buf.writeBlockPos(pos);
        });
        return ActionResult.PASS;
    }
}
