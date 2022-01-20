package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.common.component.EnergyStorageComponent;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.tile.EnergyStorageTile;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class EnergyStorageBlock extends StorageBlock {

    public EnergyStorageBlock() {
        super(Properties.create(Material.IRON).setRequiresTool().hardnessAndResistance(5, 6).sound(SoundType.METAL).harvestLevel(2).harvestTool(ToolType.PICKAXE));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergyStorageTile();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains("Tile", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT tileTag = tag.getCompound("Tile");
            int energy = tileTag.getCompound("Battery").getInt("Energy");

            tooltip.add(TranslationKeys.ENERGY_STORED.text(energy));

            EnergyStorageComponent component = NBTUtils.deserializeComponent(tileTag, "Component");
            tooltip.add(TranslationKeys.ENERGY_STORAGE_CAPACITY.text(component.getStorageCapacity()));
            tooltip.add(TranslationKeys.ENERGY_TRANSFER_RATE.text(component.getStorageTransfer()));
        } else {
            tooltip.add(TranslationKeys.ENERGY_STORED.text(0));
        }
    }
}
