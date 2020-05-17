package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.debug.DebugInfoBuilder;
import io.github.ramboxeu.techworks.common.debug.DebugInfoRenderer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class DebugResponsePacket {
    private List<DebugInfoBuilder.Section> sectionList;

    public DebugResponsePacket(List<DebugInfoBuilder.Section> sectionList) {
        this.sectionList = sectionList;
    }

    public static void encode(DebugResponsePacket packet, PacketBuffer buffer) {
        CompoundNBT nbt = new CompoundNBT();

        for (DebugInfoBuilder.Section section : packet.sectionList) {
            nbt.putString(section.getName(), Arrays.toString(section.getLines().toArray(new String[0])));
        }

        buffer.writeCompoundTag(nbt);
    }

    public static DebugResponsePacket decode(PacketBuffer buffer) {
        CompoundNBT nbt = buffer.readCompoundTag();

        List<DebugInfoBuilder.Section> sections = new ArrayList<>();
        for (String key : nbt.keySet()) {
            DebugInfoBuilder.Section section = new DebugInfoBuilder.Section(key);
            for (String line : nbt.getString(key).replaceAll("\\[|\\]", "").split(",")) {
                section.line(line.trim());
            }
            sections.add(section);
        }


        return new DebugResponsePacket(sections);
    }

    public static class Handler {
        public static void handle(final DebugResponsePacket packet, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                DebugInfoRenderer.setServerSideSections(packet.sectionList);
            });
            context.get().setPacketHandled(true);
        }
    }
}
