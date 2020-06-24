package io.github.ramboxeu.techworks.common.block.machine;

import io.github.ramboxeu.techworks.common.blockentity.machine.BoilerBlockEntity;
import io.github.ramboxeu.techworks.common.container.ExtendedScreenHandlerProvider;
import io.github.ramboxeu.techworks.common.container.machine.BoilerContainer;
import io.github.ramboxeu.techworks.common.registry.TechworksBlockEntities;
import io.github.ramboxeu.techworks.common.registry.TechworksContainers;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BoilerBlock extends AbstractMachineBlock {
    public BoilerBlock() {
        super();
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return TechworksBlockEntities.BOILER.instantiate();
    }
}