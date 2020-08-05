package io.github.ramboxeu.techworks.common.util;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentItem;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
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
                targetTe.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).ifPresent(h -> targets.add(Pair.of(h, direction)));
            }
        }

        if (targets.isEmpty()) {
            return null;
        }

        int totalAmount = sourceTank.getFluidInTank(0).getAmount();

        int amount = totalAmount / targets.size();
        int remainder = totalAmount - (amount * targets.size());

        Direction tempNext = null;
        boolean shouldUseTempNext = false;

        Fluid fluid = sourceTank.getFluidInTank(0).getFluid();

        for (int i = 0; i < targets.size(); i++) {
            Pair<IFluidHandler, Direction> targetPair = targets.get(i);
            Direction targetDirection = targetPair.getRight();

            int toDrain = next == targetDirection || (shouldUseTempNext && tempNext == targetDirection) ? amount + remainder : amount;
            int filled = targetPair.getLeft().fill(new FluidStack(fluid, toDrain), IFluidHandler.FluidAction.EXECUTE);
            sourceTank.drain(filled, IFluidHandler.FluidAction.EXECUTE);

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

        return next != null ? next.rotateY() : null;
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

    @SuppressWarnings("unchecked")
    public static <T> T castOrDefault(Object value, T other) {
        try {
            return (T)value;
        } catch (ClassCastException e) {
            return other;
        }
    }

    public static String stringifyFluidStack(FluidStack stack) {
        try {
            return String.format("Fluid: %s, %dmb",
                    ForgeRegistries.FLUIDS.getKey(stack.getFluid()),
                    stack.getAmount());
        } catch (NullPointerException e) {
            return "Fluid: null";
        }
    }

    public static ITextComponent getFluidName(FluidStack stack) {
        if (stack.isEmpty()) {
            // Technically this could be fluid.minecraft.empty,
            // but to avoid any possible problems techworks namespace is used
            return new TranslationTextComponent("fluid.techworks.empty");
        } else {
            return stack.getFluid().getAttributes().getDisplayName(stack);
        }
    }

    public static float clampFloat(float a) {
        if (a > 1) {
            return 1;
        } else if (a < 0) {
            return 0;
        } else {
            return a;
        }
    }

    public static List<ItemStack> concatItemHandlers(IItemHandler ...handlers) {
        ArrayList<ItemStack> stacks = new ArrayList<>();

        for (IItemHandler handler : handlers) {
            for (int i = 0; i < handler.getSlots(); i++) {
                stacks.add(handler.getStackInSlot(i));
            }
        }

        return stacks;
    }

    public static int getDirectionBinIndex(Direction direction) {
        switch (direction) {
            case DOWN:
                return 1;
            case UP:
                return 2;
            case NORTH:
                return 4;
            case SOUTH:
                return 8;
            case WEST:
                return 16;
            case EAST:
                return 32;
        }

        // This shouldn't happen, ever
        return -1;
    }
}
