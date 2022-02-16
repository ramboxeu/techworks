package io.github.ramboxeu.techworks.common.util;

import io.github.ramboxeu.techworks.common.fluid.handler.FluidHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class FluidUtils {

    public static FluidStack firstNotEmpty(IFluidHandler handler, int maxAmount) {
        for (int i = 0; i < handler.getTanks(); i++) {
            FluidStack stack = handler.getFluidInTank(i);

            if (!stack.isEmpty())
                return handler.drain(maxAmount, IFluidHandler.FluidAction.EXECUTE);
        }

        return FluidStack.EMPTY;
    }

    public static boolean onHandlerInteraction(PlayerEntity player, Hand hand, FluidHandler tank) {
        ItemStack held = player.getHeldItem(hand);

        if (!held.isEmpty()) {
            return held.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                    .map(bucket -> {
                        boolean isBucket = ItemUtils.isBucket(held);

                        if (!bucketTransfer(tank, bucket, player, isBucket))
                            return false;

                        ItemStack result = bucket.getContainer();

                        if (held.getCount() == 1) {
                            player.setHeldItem(hand, result);
                        } else {
                            ItemHandlerHelper.giveItemToPlayer(player, result);
                            held.shrink(1);
                            player.setHeldItem(hand, held);

                        }

                        return true;
                    })
                    .orElse(false);
        }

        return false;
    }

    public static boolean bucketTransfer(FluidHandler tank, IFluidHandler bucket, PlayerEntity player, boolean isBucket) {
        // fill the bucket
        {
            FluidStack drained = tank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE, isBucket);
            if (!drained.isEmpty()) {
                int filled = bucket.fill(drained, IFluidHandler.FluidAction.EXECUTE);

                if (filled > 0) {
                    tank.drain(filled, IFluidHandler.FluidAction.EXECUTE, isBucket);

                    SoundEvent soundevent = drained.getFluid().getAttributes().getFillSound(drained);
                    player.world.playSound(null, player.getPosX(), player.getPosY() + 0.5, player.getPosZ(), soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    return true;
                }
            }
        }

        // fill the tank
        {
            FluidStack drained = bucket.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
            if (!drained.isEmpty()) {
                int filled = tank.fill(drained, IFluidHandler.FluidAction.EXECUTE, isBucket);

                if (filled > 0) {
                    drained.setAmount(filled);
                    bucket.drain(drained, IFluidHandler.FluidAction.EXECUTE);

                    SoundEvent soundevent = drained.getFluid().getAttributes().getEmptySound(drained);
                    player.world.playSound(null, player.getPosX(), player.getPosY() + 0.5, player.getPosZ(), soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    return true;
                }
            }
        }

        return false;
    }
}
