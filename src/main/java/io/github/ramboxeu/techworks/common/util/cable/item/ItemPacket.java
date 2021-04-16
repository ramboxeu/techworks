package io.github.ramboxeu.techworks.common.util.cable.item;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.util.DirectionUtils;
import io.github.ramboxeu.techworks.common.util.cable.network.ICablePacket;
import io.github.ramboxeu.techworks.common.util.cable.network.IEndpointNode;
import io.github.ramboxeu.techworks.common.util.cable.network.INode;
import io.github.ramboxeu.techworks.common.util.cable.network.PacketType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class ItemPacket implements ICablePacket {
    private final ItemStack stack;
    private final List<Direction> offsets;
    private final IEndpointNode target;
    private final Direction previous;
    private final Direction next;
    private final ItemCableNetwork network;
    private final int transferTime;

    private INode current;
    private int index = 1;
    private int time;

    public ItemPacket(ItemStack stack, List<Direction> offsets, INode origin, IEndpointNode target, ItemCableNetwork network) {
        this.stack = stack;
        this.offsets = offsets;
        this.target = target;
        this.network = network;

        previous = null;
        next = null;
        transferTime = 1;
        current = origin;

        current.onPacketArrived(this);
    }

    public ItemPacket(Direction previous, Direction next, ItemStack stack, int transferTime) {
        this.stack = stack;
        this.previous = previous;
        this.next = next;
        this.transferTime = transferTime;
        this.network = null;

//        Techworks.LOGGER.debug("Prev = {} Next = {}", previous, next);

        offsets = null;
        target = null;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void tick() {
        time++;
    }

    public boolean transport() {
        if (index >= offsets.size())
            return true;

        Direction offset = offsets.get(index);
        current.onPacketDeparted(this);
        current = current.getNeighboursMap().get(offset);

        if (current.equals(target)) {
            if (network.doesEndpointExits(target)) {
                target.insert(this, offset.getOpposite());
            } else {
                Techworks.LOGGER.error("Target on {} doesn't exist, deleting packed. THIS IS A BUG", target.getPosition());
            }

            return true;
        } else {
            index++;
        }

        current.onPacketArrived(this);
        time = 0;

        return false;
    }

    public boolean isReady() {
        return time == current.getTransferTime();
    }

    public List<Direction> getOffsets() {
        return offsets;
    }

    private static final Vector3d CENTER = new Vector3d(0.5, 0.359375, 0.5);

    public Vector3d nextTransform(float partialTicks) {
//        if (index >= offsets.size()) {
//            return Vector3d.ZERO;
//        }

        int t;
        Vector3d vec;
        Direction dir;

        if (time / (float)transferTime >= 0.5) {
            t = time - (transferTime / 2)/* Max time / 2 */;
            dir = next;
            vec = getCenterVec();
        } else {
            t = time;
            dir = previous;
            vec = getInitialVec(dir);
        }

        switch (dir.getAxis()) {
            case X: return vec.add((DirectionUtils.isPositive(dir) ? 0.05 : -0.05) * t, 0, 0);
            case Y: return vec.add(0, (DirectionUtils.isPositive(dir) ? (0.05) : (-0.05)) * t, 0);
            case Z: return vec.add(0, 0, (DirectionUtils.isPositive(dir) ? 0.05 : -0.05) * t);
        }

        return vec;
    }

    private static Vector3d getCenterVec() {
        double x = 0;
        double y = 0 - (0.125 - (0.125 / 4));
        double z = 0;

        return new Vector3d(x + 0.5, y + 0.5/*+ 0.359375*//* - 0.046875*/, z + 0.5);
    }

    private static Vector3d getInitialVec(Direction dir) {
        double x = 0;
        double y = 0 - (0.125 - (0.125 / 4));
        double z = 0;

        switch (dir.getAxis()) {
            case X: return new Vector3d(DirectionUtils.isPositive(dir) ? x : x + 1, y + 0.5, z + 0.5);
            case Y: return new Vector3d(x + 0.5, DirectionUtils.isPositive(dir) ? y : y + 1, z + 0.5);
            case Z: return new Vector3d(x + 0.5, y + 0.5, DirectionUtils.isPositive(dir) ? z : z + 1);
        }


        return new Vector3d(x + 0.5, y + 0.359375, z + 0.5);
    }

    @Override
    public PacketType getType() {
        return PacketType.ITEM;
    }

    @Override
    public Object getPayload() {
        return stack;
    }

    @Override
    public void writeToPacket(PacketBuffer buffer) {
        buffer.writeEnumValue(offsets.get(index));
        buffer.writeEnumValue(offsets.get(index - 1));
        buffer.writeItemStack(stack);
        buffer.writeByte(transferTime);
    }
}
