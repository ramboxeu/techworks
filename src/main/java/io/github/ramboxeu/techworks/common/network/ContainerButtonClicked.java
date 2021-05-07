package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.client.container.BaseContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ContainerButtonClicked {

    private final int widgetId;
    private final int buttonId;

    public ContainerButtonClicked(int widgetId, int buttonId) {
        this.widgetId = widgetId;
        this.buttonId = buttonId;
    }

    public static void encode(ContainerButtonClicked packet, PacketBuffer buffer) {
        buffer.writeShort(packet.widgetId);
        buffer.writeShort(packet.buttonId);
    }

    public static ContainerButtonClicked decode(PacketBuffer buffer) {
        int widgetId = buffer.readShort();
        int buttonId = buffer.readShort();
        return new ContainerButtonClicked(widgetId, buttonId);
    }

    public static void handle(ContainerButtonClicked packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity player = context.get().getSender();

            if (player != null) {
                Container container = player.openContainer;

                if (container instanceof BaseContainer) {
                    ((BaseContainer) container).onButtonClicked(packet.widgetId, packet.buttonId);
                }
            }
        });
        context.get().setPacketHandled(true);
    }

}
