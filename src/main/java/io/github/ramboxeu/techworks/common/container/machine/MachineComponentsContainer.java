package io.github.ramboxeu.techworks.common.container.machine;

import io.github.ramboxeu.techworks.common.api.component.ComponentInventory;
import io.github.ramboxeu.techworks.common.blockentity.machine.AbstractMachineBlockEntity;
import io.github.ramboxeu.techworks.common.container.ComponentSlot;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MachineComponentsContainer extends ScreenHandler {
    protected PlayerInventory playerInventory;

    private final AbstractMachineBlockEntity machineBlockEntity;
    private final List<ComponentSlot> componentSlotList;
    private final Text name;

    private static final int BASE_X = 7;
    private static final int BASE_Y = 17;
    private static final int SLOT_WIDTH = 18;
    private static final int SLOT_HEIGHT = 18;
    private static final int SPACING = 0;
    private static final int RIGHT_BOUND = 168;//137 + SPACING;

    public MachineComponentsContainer(int syncId, PlayerInventory playerInventory, AbstractMachineBlockEntity machineBlockEntity, Text name) {
        super(null, syncId);
        this.machineBlockEntity = machineBlockEntity;
        this.playerInventory = playerInventory;
        this.name = name;

        ComponentInventory componentInventory = machineBlockEntity.getComponentList();
        componentSlotList = new ArrayList<>();

        // Make slots wrap
        int xMultiplier = 0;
        int yMultiplier = 0;
        for (int i = 0; i < componentInventory.size(); i++) {
            int x = ((SLOT_WIDTH + SPACING) * xMultiplier) + BASE_X;

            if (x + SLOT_WIDTH >= RIGHT_BOUND) {
                xMultiplier = 0;
                yMultiplier++;
                x = ((SLOT_WIDTH + SPACING) * xMultiplier) + BASE_X;
            }

            int y = ((SLOT_HEIGHT + SPACING)* yMultiplier) + BASE_Y;

            this.addComponentSlot(new ComponentSlot(componentInventory, i, x, y));

            xMultiplier++;
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    private void addComponentSlot(ComponentSlot slot) {
        this.componentSlotList.add(slot);
        this.addSlot(slot);
    }

    public List<ComponentSlot> getComponentSlotList() {
        return componentSlotList;
    }

    public Text getName() {
        return name;
    }

    public static MachineComponentsContainer factory(int syncId, Identifier identifier, PlayerEntity playerEntity, PacketByteBuf packetByteBuf) {
        BlockEntity blockEntity = playerEntity.world.getBlockEntity(packetByteBuf.readBlockPos());

        if (blockEntity instanceof AbstractMachineBlockEntity) {
            AbstractMachineBlockEntity machine = (AbstractMachineBlockEntity)blockEntity;
            return new MachineComponentsContainer(syncId, playerEntity.inventory, machine, machine.getComponentsContainerName());
        }

        return null;
    }
}
