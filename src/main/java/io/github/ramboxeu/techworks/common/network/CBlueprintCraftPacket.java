package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.client.container.BlueprintTableContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class CBlueprintCraftPacket {
    private final ResourceLocation blueprint;

    public CBlueprintCraftPacket(ResourceLocation blueprint) {
        this.blueprint = blueprint;
    }

    public static CBlueprintCraftPacket decode(PacketBuffer buffer) {
        return new CBlueprintCraftPacket(buffer.readResourceLocation());
    }

    public static void encode(CBlueprintCraftPacket packet, PacketBuffer buffer) {
        buffer.writeResourceLocation(packet.blueprint);
    }

    public static void handle(CBlueprintCraftPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity sender = context.get().getSender();
            if (sender.openContainer instanceof BlueprintTableContainer) {
                ((BlueprintTableContainer) sender.openContainer).craftBlueprint(ForgeRegistries.ITEMS.getValue(packet.blueprint));
            }
        });
        context.get().setPacketHandled(true);
    }
}
