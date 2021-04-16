package io.github.ramboxeu.techworks.client.util;

import io.github.ramboxeu.techworks.common.util.cable.connection.CableConnection;

public class TextureCoordsGroup {

    private final Connector connector;
    private final Connected connected;
    private final Connection connection;
    private final Disconnected disconnected;

    private TextureCoordsGroup(Connector connector, Connected connected, Connection connection, Disconnected disconnected) {
        this.connector = connector;
        this.connected = connected;
        this.connection = connection;
        this.disconnected = disconnected;
    }

    public Connector getConnector() {
        return connector;
    }

    public Connected getConnected() {
        return connected;
    }

    public Connection getConnection() {
        return connection;
    }

    public Disconnected getDisconnected() {
        return disconnected;
    }

    public static TextureCoordsGroup of(Connector connector, Connected connected, Connection connection, Disconnected disconnected) {
        return new TextureCoordsGroup(connector, connected, connection, disconnected);
    }

    public static TextureCoordsGroup of(TextureCoords connectorBack,
                                        TextureCoords connectorHorizontal,
                                        TextureCoords connectorVertical,
                                        TextureCoords connectorInputHorizontal,
                                        TextureCoords connectorInputVertical,
                                        TextureCoords connectorOutputHorizontal,
                                        TextureCoords connectorOutputVertical,
                                        TextureCoords connectedHorizontal,
                                        TextureCoords connectedVertical,
                                        TextureCoords connectionHorizontal,
                                        TextureCoords connectionVertical,
                                        TextureCoords disconnected,
                                        TextureCoords blocked) {
        return of(Connector.of(connectorBack, connectorHorizontal, connectorVertical, connectorInputHorizontal, connectorInputVertical, connectorOutputHorizontal, connectorOutputVertical),
                Connected.of(connectedHorizontal, connectedVertical),
                Connection.of(connectionHorizontal, connectionVertical),
                Disconnected.of(disconnected, blocked));
    }

    public static TextureCoordsGroup of(TextureCoords connectorBack,
                                        TextureCoords connectorSide,
                                        TextureCoords connectorInputSide,
                                        TextureCoords connectorOutputSide,
                                        TextureCoords connectedSide,
                                        TextureCoords connectionSide,
                                        TextureCoords disconnected,
                                        TextureCoords blocked) {
        return of(connectorBack,
                connectorSide,
                connectorSide,
                connectorInputSide,
                connectorInputSide,
                connectorOutputSide,
                connectorOutputSide,
                connectedSide,
                connectedSide,
                connectionSide,
                connectionSide,
                disconnected,
                blocked);
    }

    public static final class Connector {
        public final TextureCoords back;
        public final TextureCoords horizontal;
        public final TextureCoords vertical;
        public final TextureCoords inputHorizontal;
        public final TextureCoords inputVertical;
        public final TextureCoords outputHorizontal;
        public final TextureCoords outputVertical;

        private Connector(TextureCoords back, TextureCoords horizontal, TextureCoords vertical, TextureCoords inputHorizontal, TextureCoords inputVertical, TextureCoords outputHorizontal, TextureCoords outputVertical) {
            this.back = back;
            this.horizontal = horizontal;
            this.vertical = vertical;
            this.inputHorizontal = inputHorizontal;
            this.inputVertical = inputVertical;
            this.outputHorizontal = outputHorizontal;
            this.outputVertical = outputVertical;
        }

        public static Connector of(TextureCoords back, TextureCoords horizontal, TextureCoords vertical, TextureCoords inputHorizontal, TextureCoords inputVertical, TextureCoords outputHorizontal, TextureCoords outputVertical) {
            return new Connector(back, horizontal, vertical, inputHorizontal, inputVertical, outputHorizontal, outputVertical);
        }
    }

    public static final class Connected {
        public final TextureCoords horizontal;
        public final TextureCoords vertical;

        private Connected(TextureCoords horizontal, TextureCoords vertical) {
            this.horizontal = horizontal;
            this.vertical = vertical;
        }

        public static Connected of(TextureCoords horizontal, TextureCoords vertical) {
            return new Connected(horizontal, vertical);
        }

        public static Connected of(TextureCoords side) {
            return new Connected(side, side);
        }
    }

    public static final class Connection {
        public final TextureCoords horizontal;
        public final TextureCoords vertical;

        private Connection(TextureCoords horizontal, TextureCoords vertical) {
            this.horizontal = horizontal;
            this.vertical = vertical;
        }

        public static Connection of(TextureCoords horizontal, TextureCoords vertical) {
            return new Connection(horizontal, vertical);
        }

        public static Connection of(TextureCoords side) {
            return new Connection(side, side);
        }
    }

    public static final class Disconnected {
        public final TextureCoords disconnected;
        public final TextureCoords blocked;

        private Disconnected(TextureCoords disconnected, TextureCoords blocked) {
            this.disconnected = disconnected;
            this.blocked = blocked;
        }

        public TextureCoords chooseCoords(CableConnection conn) {
            return  conn.getStatus().isBlocked() ? blocked : disconnected;
        }

        public static Disconnected of(TextureCoords disconnected, TextureCoords blocked) {
            return new Disconnected(disconnected, blocked);
        }
    }
}
