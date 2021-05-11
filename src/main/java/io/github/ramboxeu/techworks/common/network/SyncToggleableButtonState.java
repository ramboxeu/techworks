package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.client.container.BaseContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncToggleableButtonState {
    private final int slotId;
    private final boolean enabled;

    public SyncToggleableButtonState(int slotId, boolean enabled) {
        this.slotId = slotId;
        this.enabled = enabled;
    }

    public static void encode(SyncToggleableButtonState packet, PacketBuffer buffer) {
        buffer.writeShort(packet.slotId);
        buffer.writeBoolean(packet.enabled);
    }

    public static SyncToggleableButtonState decode(PacketBuffer buffer) {
        int slotId = buffer.readShort();
        boolean enabled = buffer.readBoolean();

        return new SyncToggleableButtonState(slotId, enabled);
    }

    public static void handle(SyncToggleableButtonState packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity player = context.get().getSender();
            Container container = player.openContainer;

            if (container instanceof BaseContainer) {
                ((BaseContainer) container).onToggleSlot(packet.slotId, packet.enabled);
            }
        });
        context.get().setPacketHandled(true);
    }

}
