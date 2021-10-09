package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.widget.display.EnergyDisplayWidget;
import io.github.ramboxeu.techworks.client.screen.widget.inventory.SlotWidget;
import io.github.ramboxeu.techworks.client.screen.widget.progress.ArrowProgressWidget;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.machine.ElectricFurnaceTile;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ElectricFurnaceContainer extends BaseMachineContainer<ElectricFurnaceTile> {

    public ElectricFurnaceContainer(int id, PlayerInventory inv, ElectricFurnaceTile tile) {
        this(id, inv, tile, IWorldPosCallable.DUMMY);
    }

    public ElectricFurnaceContainer(int id, PlayerInventory playerInventory, ElectricFurnaceTile tile, IWorldPosCallable callable) {
        super(TechworksContainers.ELECTRIC_FURNACE.get(), id, playerInventory, tile);

        addWidget(new EnergyDisplayWidget(this, 8, 14, tile.getBatteryData()));
        addWidget(new ArrowProgressWidget(81, 35, false, tile::getEnergy, tile::getExtractedEnergy));
        addWidget(new SlotWidget(this, 55, 34, 0, false, tile.getInvData()));
        addWidget(new SlotWidget(this, 111, 30, 0,  true, tile.getOutputInvData(),
                (handler, index, x, y) -> new SlotItemHandler(handler, index, x, y) {
                    @Override
                    public boolean isItemValid(@Nonnull ItemStack stack) {
                        return false;
                    }

                    @Override
                    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
                        callable.consume((world, pos) -> {
                            if (!world.isRemote) {
                                float xp = tile.resetXP();

                                double x = pos.getX();
                                double y = pos.getY();
                                double z = pos.getZ();

                                while (xp > 0) {
                                    int orbXP = ExperienceOrbEntity.getXPSplit((int) xp);
                                    xp -= orbXP;
                                    world.addEntity(new ExperienceOrbEntity(world, x, y, z, orbXP));
                                }
                            }
                        });

                        return super.onTake(player, stack);
                    }
                })
        );
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }
}
