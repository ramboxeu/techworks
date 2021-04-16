//package io.github.ramboxeu.techworks.common.util.cable.network;
//
//import java.util.Objects;
//import java.util.UUID;
//
//public class CableNetworkHolder<T extends ICableNetwork> {
//    private T network;
//    private UUID id;
//
//    CableNetworkHolder(T network, UUID id) {
//        this.network = network;
//        this.id = id;
//    }
//
//    @SuppressWarnings("unchecked")
//    void changeNetwork(UUID id, ICableNetwork network) {
//        this.id = id;
//        this.network = (T)network;
//    }
//
//    public T get() {
//        return network;
//    }
//
//    public UUID getId() {
//        return id;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (o instanceof CableNetworkHolder) {
//            return ((CableNetworkHolder<?>) o).network.equals(network);
//        }
//
//        if (o instanceof CableNetwork) {
//            return o.equals(network);
//        }
//
//        return false;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(network, id);
//    }
//
//    @Override
//    public String toString() {
//        return "CableNetworkHolder@" + hashCode() + "{" +
//                "network=" + network +
//                ", id=" + id +
//                '}';
//    }
//}
