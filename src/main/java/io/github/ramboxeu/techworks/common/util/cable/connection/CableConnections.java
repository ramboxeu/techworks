package io.github.ramboxeu.techworks.common.util.cable.connection;

import io.github.ramboxeu.techworks.common.network.IPacketSerializable;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.EnumMap;
import java.util.Map;

public class CableConnections implements INBTSerializable<CompoundNBT>, IPacketSerializable {

    public static final CableConnections DEFAULT = new CableConnections(newCableModelDefaultMap());

    private final Map<Direction, CableConnection> connections;

    public CableConnections() {
        this(newDefaultMap());
    }

    private CableConnections(Map<Direction, CableConnection> connections) {
        this.connections = connections;
    }

    public boolean isDownConnected() {
        return isConnected(Direction.DOWN);
    }

    public boolean isUpConnected() {
        return isConnected(Direction.UP);
    }

    public boolean isNorthConnected() {
        return isConnected(Direction.NORTH);
    }

    public boolean isSouthConnected() {
        return isConnected(Direction.SOUTH);
    }

    public boolean isWestConnected() {
        return isConnected(Direction.WEST);
    }

    public boolean isEastConnected() {
        return isConnected(Direction.EAST);
    }

    public boolean isConnected(Direction direction) {
        return connections.get(direction).getStatus().isConnected();
    }

    public void setStatus(Direction direction, boolean connected) {
        setStatus(direction, connected ? ConnectionStatus.CONNECTED : ConnectionStatus.DISCONNECTED);
    }

    public void setStatus(Direction direction, ConnectionStatus status) {
        connections.get(direction).setStatus(status);
    }

    public ConnectionStatus getStatus(Direction direction) {
        return connections.get(direction).getStatus();
    }

    public ConnectionMode getMode(Direction direction) {
        return connections.get(direction).getMode();
    }

    public void setMode(Direction direction, ConnectionMode mode) {
        connections.get(direction).setMode(mode);
    }

    public void setConnection(Direction direction, ConnectionMode mode, boolean connected) {
        setConnection(direction, mode, connected ? ConnectionStatus.CONNECTED : ConnectionStatus.DISCONNECTED);
    }

    public void setConnection(Direction direction, ConnectionMode mode, ConnectionStatus status) {
        connections.get(direction).set(mode, status);
    }

    public CableConnection getConnection(Direction direction) {
        return connections.get(direction);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        for (Direction direction : Direction.values()) {
            serializeDir(tag, direction);
        }

        return tag;
    }

    private void serializeDir(CompoundNBT rootTag, Direction direction) {
        CableConnection connection = connections.get(direction);
        CompoundNBT tag = new CompoundNBT();

        NBTUtils.serializeEnum(tag, "Mode", connection.getMode());
        NBTUtils.serializeEnum(tag, "Status", connection.getStatus());

        rootTag.put(direction.getName2(), tag);
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        for (Direction direction : Direction.values()) {
            deserializeDir(tag, direction);
        }
    }

    private void deserializeDir(CompoundNBT rootTag, Direction direction) {
        CompoundNBT tag = rootTag.getCompound(direction.getName2());

        ConnectionMode mode = NBTUtils.deserializeEnum(tag, "Mode", ConnectionMode.class).orElse(ConnectionMode.BOTH);
        ConnectionStatus status = NBTUtils.deserializeEnum(tag, "Status", ConnectionStatus.class).orElse(ConnectionStatus.DISCONNECTED);

        connections.put(direction, new CableConnection(mode, status));
    }

    @Override
    public void serializePacket(PacketBuffer buf) {
        byte[] encodedModes = new byte[6];
        byte[] encodedStatues = new byte[6];

        for (Direction direction : Direction.values()) {
            CableConnection connection = connections.get(direction);
            int ordinal = direction.ordinal();

            encodedStatues[ordinal] = (byte) connection.getStatus().ordinal();
            encodedModes[ordinal] = (byte) connection.getMode().ordinal();
        }

        buf.writeByteArray(encodedStatues);
        buf.writeByteArray(encodedModes);
    }

    @Override
    public void deserializePacket(PacketBuffer buf) {
        byte[] encodedStatues = buf.readByteArray();
        byte[] encodedModes = buf.readByteArray();

        ConnectionMode[] modes = ConnectionMode.values();
        ConnectionStatus[] statuses = ConnectionStatus.values();

        for (Direction direction : Direction.values()) {
            int ordinal = direction.ordinal();

            ConnectionStatus status = statuses[encodedStatues[ordinal]];
            ConnectionMode mode = modes[encodedModes[ordinal]];

            connections.put(direction, new CableConnection(mode, status));
        }
    }

    private static Map<Direction, CableConnection> newDefaultMap() {
        EnumMap<Direction, CableConnection> map = new EnumMap<>(Direction.class);

        map.put(Direction.DOWN, CableConnection.newDefault());
        map.put(Direction.UP, CableConnection.newDefault());
        map.put(Direction.NORTH, CableConnection.newDefault());
        map.put(Direction.SOUTH, CableConnection.newDefault());
        map.put(Direction.WEST, CableConnection.newDefault());
        map.put(Direction.EAST, CableConnection.newDefault());

        return map;
    }

    private static Map<Direction, CableConnection> newCableModelDefaultMap() {
        EnumMap<Direction, CableConnection> map = new EnumMap<>(Direction.class);

        map.put(Direction.DOWN, new CableConnection(ConnectionMode.CONNECTION, ConnectionStatus.CONNECTED));
        map.put(Direction.UP, new CableConnection(ConnectionMode.CONNECTION, ConnectionStatus.CONNECTED));
        map.put(Direction.NORTH, new CableConnection(ConnectionMode.BOTH, ConnectionStatus.DISCONNECTED));
        map.put(Direction.SOUTH, new CableConnection(ConnectionMode.BOTH, ConnectionStatus.DISCONNECTED));
        map.put(Direction.WEST, new CableConnection(ConnectionMode.BOTH, ConnectionStatus.DISCONNECTED));
        map.put(Direction.EAST, new CableConnection(ConnectionMode.BOTH, ConnectionStatus.DISCONNECTED));

        return map;
    }

}
