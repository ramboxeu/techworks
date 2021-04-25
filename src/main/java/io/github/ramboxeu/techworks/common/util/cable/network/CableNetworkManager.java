package io.github.ramboxeu.techworks.common.util.cable.network;

import io.github.ramboxeu.techworks.common.util.NBTUtils;
import io.github.ramboxeu.techworks.common.util.cable.energy.EnergyCableNetwork;
import io.github.ramboxeu.techworks.common.util.cable.energy.EnergyNetworkHolder;
import io.github.ramboxeu.techworks.common.util.cable.fluid.FluidCableNetwork;
import io.github.ramboxeu.techworks.common.util.cable.fluid.FluidNetworkHolder;
import io.github.ramboxeu.techworks.common.util.cable.item.ItemCableNetwork;
import io.github.ramboxeu.techworks.common.util.cable.item.ItemNetworkHolder;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.*;

public class CableNetworkManager extends WorldSavedData {

    private static final String NAME = "techworks_cable_networks";

    private final HashMap<UUID, ICableNetwork> networks = new HashMap<>();
    private final HashMap<UUID, ICableNetworkHolder> holders = new HashMap<>();

    public CableNetworkManager() {
        super(NAME);
    }

    @Override
    public void read(CompoundNBT tag) {
        ListNBT listTag = tag.getList("Networks", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < listTag.size(); i++) {
            CompoundNBT networkTag = listTag.getCompound(i);
            UUID id = networkTag.getUniqueId("Id");
            NetworkType type = NBTUtils.deserializeEnum(networkTag, "NetworkType", NetworkType.class);
            networks.put(id, type.createNetwork(id));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        ListNBT listTag = new ListNBT();
        int i = 0;

        for (Map.Entry<UUID, ICableNetwork> entry : networks.entrySet()) {
            CompoundNBT networkTag = new CompoundNBT();
            ICableNetwork network = entry.getValue();

            networkTag.putUniqueId("Id", entry.getKey());
            NBTUtils.serializeEnum(networkTag, "NetworkType", network.getType());
            listTag.set(i++, networkTag);
        }

        tag.put("Networks", listTag);
        return tag;
    }

    // TODO: Network garbage collection

    public void mergeNetworks(@Nonnull ICableNetwork primary, @Nonnull ICableNetwork secondary) {
        if (primary.getType() != secondary.getType())
            return;

        ICableNetwork from;
        ICableNetwork to;

        Collection<IEndpointNode> primaryEndpoints = primary.getAllEndpoints();
        Collection<IEndpointNode> secondaryEndpoints = secondary.getAllEndpoints();

        if (primaryEndpoints.isEmpty() || primaryEndpoints.size() <= secondaryEndpoints.size()) {
            from = primary;
            to = secondary;
        } else {
            from = secondary;
            to = primary;
        }

        Collection<IEndpointNode> fromEndpoints = from.getAllEndpoints();
        Collection<IEndpointNode> toEndpoints = to.getAllEndpoints();

        fromEndpoints.stream()
                .filter(node -> !toEndpoints.contains(node))
                .forEach(toEndpoints::add);

        UUID id = to.getId();
        ICableNetwork network = networks.get(id);

        holders.get(from.getId()).update(id, network);
    }

    // This isn't particularly efficient
    public void splitNetwork(ICableNetwork network, List<INode> splitPoints) {
        boolean flag = true;

        for (Iterator<INode> iter = splitPoints.iterator(); iter.hasNext(); ) {
            INode point = iter.next();
            iter.remove();

            List<INode> set = new ArrayList<>();
            List<IEndpointNode> endpoints = new ArrayList<>();

            ListIterator<INode> listIter = set.listIterator();
            listIter.add(point);

            while (listIter.hasPrevious()) {
                INode node = listIter.previous();

                if (node.isTraversable()) {
                    for (INode neighbour : node.getNeighbours()) {
                        splitPoints.remove(neighbour);

                        if (flag && splitPoints.isEmpty())
                            return;

                        if (!set.contains(neighbour)) {
                            listIter.add(neighbour);

                            if (neighbour instanceof IEndpointNode)
                                endpoints.add((IEndpointNode) neighbour);
                        }
                    }
                }
            }

            UUID id = UUID.randomUUID();
            ICableNetwork splitNetwork = createNetwork(network.getType(), id, endpoints);
            ICableNetworkHolder splitHolder = createHolder(network.getType(), splitNetwork, id);

            // There will very likely be problems with holders
            networks.put(id, splitNetwork);
            holders.put(id, splitHolder);

            for (INode node : set)
                node.setHolder(splitHolder);

            flag = false;
        }

        networks.remove(network.getId());
    }

    private ICableNetworkHolder createHolder(NetworkType type, ICableNetwork splitNetwork, UUID id) {
        switch (type) {
            case ITEM:
                return new ItemNetworkHolder((ItemCableNetwork) splitNetwork, id);
            case LIQUID:
                return FluidNetworkHolder.liquid((FluidCableNetwork) splitNetwork, id);
        }

        return null;
    }

    private ICableNetwork createNetwork(NetworkType type, UUID id, List<IEndpointNode> endpoints) {
        switch (type) {
            case ITEM:
                return new ItemCableNetwork(id, endpoints);
            case LIQUID:
                return FluidCableNetwork.liquid(id, endpoints);
        }

        return null;
    }

    public void tickNetworks() {
        networks.forEach((id, network) -> { if (network != null) network.tick(); });
    }

    public ItemNetworkHolder getItemNetwork(UUID networkId) {
        if (holders.containsKey(networkId)) {
            ICableNetworkHolder holder = holders.get(networkId);

            return (ItemNetworkHolder) holder;
        }

        UUID id = networkId == null ? UUID.randomUUID() : networkId;
        ItemCableNetwork network = new ItemCableNetwork(id);
        ItemNetworkHolder holder = new ItemNetworkHolder(network, id);

        networks.put(id, network);
        holders.put(id, holder);

        return holder;
    }

    public FluidNetworkHolder getLiquidNetwork(UUID networkId) {
        if (holders.containsKey(networkId)) {
            ICableNetworkHolder holder = holders.get(networkId);

            return (FluidNetworkHolder) holder;
        }

        UUID id = networkId == null ? UUID.randomUUID() : networkId;
        FluidCableNetwork network = FluidCableNetwork.liquid(id);
        FluidNetworkHolder holder = FluidNetworkHolder.liquid(network, id);

        networks.put(id, network);
        holders.put(id, holder);

        return holder;
    }

    private FluidNetworkHolder getGasNetwork(UUID networkId) {
        if (holders.containsKey(networkId)) {
            ICableNetworkHolder holder = holders.get(networkId);

            return (FluidNetworkHolder) holder;
        }

        UUID id = networkId == null ? UUID.randomUUID() : networkId;
        FluidCableNetwork network = FluidCableNetwork.gas(id);
        FluidNetworkHolder holder = FluidNetworkHolder.gas(network, id);

        networks.put(id, network);
        holders.put(id, holder);

        return holder;
    }

    public EnergyNetworkHolder getEnergyNetwork(UUID networkId) {
        if (holders.containsKey(networkId)) {
            ICableNetworkHolder holder = holders.get(networkId);

            return (EnergyNetworkHolder) holder;
        }

        UUID id = networkId == null ? UUID.randomUUID() : networkId;
        EnergyCableNetwork network = new EnergyCableNetwork(id);
        EnergyNetworkHolder holder = new EnergyNetworkHolder(network, id);

        networks.put(id, network);
        holders.put(id, holder);

        return holder;
    }

    public ICableNetworkHolder getNetwork(NetworkType type, UUID networkId) {
        switch (type) {
            case GAS:
                return getGasNetwork(networkId);
            case LIQUID:
                return getLiquidNetwork(networkId);
            case ITEM:
                return getItemNetwork(networkId);
            case ENERGY:
                return getEnergyNetwork(networkId);
        }

        return null;
    }

    public ICableNetwork getNetwork(UUID id) {
        return networks.get(id);
    }

    // Server side only
    public static CableNetworkManager get(World world) {
        if (world.isRemote)
            throw new IllegalStateException("CableNetworkManager can only be obtained on the server side");

        DimensionSavedDataManager manager = ((ServerWorld) world).getSavedData();
        return manager.getOrCreate(CableNetworkManager::new, NAME);
    }
}
