package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.tile.BoilerTile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class BoilerContainer extends Container {
    private final Inventory inventory;
    private final PlayerInventory playerInventory;

    public final int id;
    public final BoilerTile boilerTile;

    public BoilerContainer(int id, PlayerInventory playerInventory, BlockPos pos) {
        super(Registration.BOILER_CONTAINER.get(), id);

        this.id = id;

        TileEntity tileEntity = Minecraft.getInstance().world.getTileEntity(pos);

        if (tileEntity instanceof BoilerTile) {
            this.inventory = ((BoilerTile)tileEntity).inventory;
            this.boilerTile = (BoilerTile) tileEntity;
        } else {
            this.inventory = null;
            this.boilerTile = null;
        }

        this.playerInventory = playerInventory;

        this.addSlot(new Slot(inventory, 0, 0, 0));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i *18, 142));
        }
    }

    public BoilerContainer(int id, PlayerInventory inventory, PacketBuffer packetBuffer) {
        this(id, inventory, packetBuffer.readBlockPos());
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ContainerType<?> getType() {
        return Registration.BOILER_CONTAINER.get();
    }
}
