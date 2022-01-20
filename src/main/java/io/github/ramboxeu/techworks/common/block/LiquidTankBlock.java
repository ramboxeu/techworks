package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.common.component.LiquidStorageComponent;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.tile.LiquidTankTile;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class LiquidTankBlock extends StorageBlock {
    private static final VoxelShape SHAPE = VoxelShapes.create(3 / 16.0, 0, 3 / 16.0, 13 / 16.0, 14 / 16.0, 13 / 16.0);

    public LiquidTankBlock() {
        super(Properties.create(Material.IRON).setRequiresTool().hardnessAndResistance(5, 6).sound(SoundType.METAL).harvestLevel(2).harvestTool(ToolType.PICKAXE));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new LiquidTankTile();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof LiquidTankTile) {
            ItemStack result = ((LiquidTankTile) tile).onRightClick(player.getHeldItem(hand), player);

            if (result != null) {
                player.setHeldItem(hand, result);
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.FAIL;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);

        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains("Tile", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT tileTag = tag.getCompound("Tile");
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tileTag.getCompound("Tank").getCompound("Fluid"));

            if (!fluid.isEmpty()) {
                tooltip.add(TranslationKeys.LIQUID_STORED.text().appendSibling(fluid.getDisplayName()));
                tooltip.add(TranslationKeys.FLUID_AMOUNT_STORED.text(fluid.getAmount()));
            } else {
                tooltip.add(TranslationKeys.LIQUID_STORED.text().appendSibling(TranslationKeys.EMPTY.text()));
            }

            LiquidStorageComponent component = NBTUtils.deserializeComponent(tileTag, "Component");
            tooltip.add(TranslationKeys.FLUID_STORAGE_CAPACITY.text(component.getStorageCapacity()));
            tooltip.add(TranslationKeys.FLUID_TRANSFER_RATE.text(component.getStorageTransfer()));
        } else {
            tooltip.add(TranslationKeys.LIQUID_STORED.text().appendSibling(TranslationKeys.EMPTY.text()));
        }
    }
}
