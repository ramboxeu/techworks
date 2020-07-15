package io.github.ramboxeu.techworks.common.util;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    private static final Random RANDOM = new Random();

    public static Direction distributeFluid(IFluidHandler sourceTank, Direction next, TileEntity sourceTe) {
        if (!sourceTe.hasWorld()) {
            return null;
        }

        BlockPos sourcePos = sourceTe.getPos();
        World world = sourceTe.getWorld();
        List<Pair<IFluidHandler, Direction>> targets = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            TileEntity targetTe = world.getTileEntity(sourcePos.offset(direction));
            if (targetTe != null) {
                targetTe.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(h -> targets.add(Pair.of(h, direction)));
            }
        }

        if (targets.isEmpty()) {
            return null;
        }

        int totalAmount = sourceTank.getFluidInTank(0).getAmount();

        int amount = totalAmount / targets.size();
        int remainder = totalAmount - (amount * targets.size());

        if (next == null) {
            next = targets.get(0).getRight();
        }

        Direction tempNext = null;
        boolean shouldUseTempNext = false;

        for (int i = 0; i < targets.size(); i++) {
            Pair<IFluidHandler, Direction> targetPair = targets.get(i);
            Direction targetDirection = targetPair.getRight();

            int toDrain = next == targetDirection || (shouldUseTempNext && tempNext == targetDirection) ? amount + remainder : amount;
            int filled = targetPair.getLeft().fill(sourceTank.drain(toDrain, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);

            if (filled < toDrain) {
                totalAmount = sourceTank.getFluidInTank(0).getAmount();
                amount = totalAmount / targets.size() - (i + 1);
                remainder = totalAmount - (amount * targets.size());

                if (remainder > 0) {
                    tempNext = targetDirection.rotateY();
                    shouldUseTempNext = true;
                }
            }
        }

        return next.rotateY();
    }

    public static void damageComponent(ItemStack componentStack) {
        if (!(componentStack.getItem() instanceof ComponentItem) || !componentStack.isDamageable()) {
            return;
        }

        ComponentItem component = (ComponentItem) componentStack.getItem();
        float chance = 1 - ((component.getLevel() * 2) / 10.0F);
        Techworks.LOGGER.debug("Component: {}{Level: {}, Durability: {}); Chance: {}", component, component.getLevel(), componentStack.getDamage(), chance);

        float rand = RANDOM.nextFloat();

        Techworks.LOGGER.debug("Rand: {}", rand);
        if (rand <= chance) {
            if (componentStack.getDamage() != componentStack.getMaxDamage() - 1) {
                componentStack.setDamage(componentStack.getDamage() + 1);
            }
        }
    }
}
