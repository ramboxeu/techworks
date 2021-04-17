package io.github.ramboxeu.techworks.common.util.model;

import io.github.ramboxeu.techworks.common.util.cable.connection.CableConnection;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class CuboidGroup {
    private final Cuboid connected;
    private final Cuboid connection;
    private final Cuboid connector;

    public CuboidGroup(Cuboid connected, Cuboid connector, Cuboid connection) {
        this.connected = connected;
        this.connector = connector;
        this.connection = connection;
    }

    public VoxelShape chooseVoxelShape(CableConnection cableConnection) {
        if (cableConnection.getStatus().isConnected())
            if (cableConnection.getMode().isConnection()) return connection.getVoxelShape();
            else return VoxelShapes.or(connected.getVoxelShape(), connector.getVoxelShape());

        return VoxelShapes.empty();
    }

    public Cuboid getConnected() {
        return connected;
    }

    public Cuboid getConnection() {
        return connection;
    }

    public Cuboid getConnector() {
        return connector;
    }

    public static CuboidGroup of(Cuboid connected, Cuboid connector, Cuboid connection) {
        return new CuboidGroup(connected, connector, connection);
    }
}
