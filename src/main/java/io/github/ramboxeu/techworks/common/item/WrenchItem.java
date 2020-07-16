package io.github.ramboxeu.techworks.common.item;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentStackHandler;
import io.github.ramboxeu.techworks.client.container.machine.ComponentsContainer;
import io.github.ramboxeu.techworks.common.component.IComponentsContainerProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class WrenchItem extends Item {

    public WrenchItem() {
        super(new Item.Properties().maxStackSize(1).group(Techworks.ITEM_GROUP));
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        Mode mode = getMode(stack);
        if (mode == Mode.NONE) {
            return ActionResultType.PASS;
        }

        if (mode == Mode.CONFIGURE && !context.getWorld().isRemote && context.getPlayer() != null) {
            TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());

            if (tileEntity instanceof IComponentsContainerProvider) {
                IComponentsContainerProvider containerProvider = (IComponentsContainerProvider) tileEntity;
                ComponentStackHandler components = containerProvider.getComponentsStackHandler();

                NetworkHooks.openGui((ServerPlayerEntity) context.getPlayer(), new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return containerProvider.getComponentsDisplayName();
                    }

                    @Nullable
                    @Override
                    public Container createMenu(int syncId, PlayerInventory playerInv, PlayerEntity player) {
                        return new ComponentsContainer(syncId, playerInv, components);
                    }
                }, tileEntity.getPos());
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.CONSUME;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            BlockRayTraceResult rayTraceResult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.ANY);

            if (rayTraceResult.getType() == RayTraceResult.Type.MISS) {
                ItemStack stack = playerIn.getHeldItem(handIn);
                int modeIndex = Mode.indexOf(getMode(stack)) + 1;

                if (modeIndex >= Mode.values().length) {
                    modeIndex = 0;
                }

                Mode mode = Mode.valueOf(modeIndex);
                setMode(stack, mode);
                notifyPlayer((ServerPlayerEntity) playerIn, mode);
                return ActionResult.resultSuccess(stack);
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        // TODO: make translate-able
        tooltip.add(new StringTextComponent("Mode: " + getMode(stack).name()));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    private void setMode(ItemStack stack, Mode mode) {
        CompoundNBT stackTag = stack.getOrCreateTag();
        stackTag.putInt("WrenchMode", Mode.indexOf(mode));
    }

    private Mode getMode(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundNBT stackTag = stack.getTag();
            if (stackTag.contains("WrenchMode", Constants.NBT.TAG_INT)) {
                return Mode.valueOf(stackTag.getInt("WrenchMode"));
            }
        }

        return Mode.ROTATE;
    }

    private void notifyPlayer(ServerPlayerEntity player, Mode mode) {
//        player.connection.sendPacket(new STitlePacket(1, 20, 1));
        // TODO: make translate-able
        player.sendStatusMessage(new StringTextComponent("Wrench mode: " + mode.name()), true);
//        player.connection.sendPacket(new STitlePacket(STitlePacket.Type.SUBTITLE, new StringTextComponent("Wrench mode: " + mode.name())));
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player) {
        return true;
    }

    public enum Mode {
        NONE,
        ROTATE,
        CONFIGURE;

        public static int indexOf(Mode mode) {
            for (int i = 0; i < values().length; i++) {
                if (values()[i] == mode) {
                    return i;
                }
            }

            throw new RuntimeException("Mode: " + mode + " was not found");
        }

        public static Mode valueOf(int index) {
            if (index >= 0 && index < values().length) {
                return values()[index];
            }

            throw new RuntimeException(index + " is outside of range [0, " + values().length + ")");
        }
    }
}
