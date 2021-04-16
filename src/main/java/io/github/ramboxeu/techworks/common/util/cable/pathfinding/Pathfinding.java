package io.github.ramboxeu.techworks.common.util.cable.pathfinding;

import io.github.ramboxeu.techworks.common.util.Pair;
import io.github.ramboxeu.techworks.common.util.Utils;
import io.github.ramboxeu.techworks.common.util.cable.network.IEndpointNode;
import io.github.ramboxeu.techworks.common.util.cable.network.INode;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class Pathfinding {

    public static Pair<Integer, Direction> timeAndFinalOffset(INode origin, INode target) {
        List<NodeWrapper> nodes = Pathfinder.run(origin, target);

        if (nodes.size() > 1) {
            int time = nodes.stream().mapToInt(NodeWrapper::getTime).sum();

            BlockPos pos1 = nodes.get(nodes.size() - 1).pos();
            BlockPos pos2 = nodes.get(nodes.size() - 2).pos();

            Direction side = Utils.getDirectionFromPos(pos2, pos1);

            return Pair.of(time, side);
        }

        return null;
    }

    public static Direction finalOffset(INode origin, IEndpointNode target) {
        List<NodeWrapper> nodes = Pathfinder.run(origin, target);

        BlockPos pos1 = nodes.get(nodes.size() - 1).pos();
        BlockPos pos2 = nodes.get(nodes.size() - 2).pos();

        return Utils.getDirectionFromPos(pos2, pos1);
    }
}
