package io.github.ramboxeu.techworks.common.util.cable.pathfinding;

import io.github.ramboxeu.techworks.common.util.MathUtils;
import io.github.ramboxeu.techworks.common.util.cable.network.IEndpointNode;
import io.github.ramboxeu.techworks.common.util.cable.network.INode;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class Pathfinder {
    private final PriorityQueue<NodeWrapper> openSet;
    private final HashSet<NodeWrapper> closedSet;
    private final HashMap<INode, NodeWrapper> wrappers;
    private final NodeWrapper target;
    private final NodeWrapper start;

    private Pathfinder(NodeWrapper start, NodeWrapper target) {
        this.start = start;
        this.target = target;

        openSet = new PriorityQueue<>();
        closedSet = new HashSet<>();
        wrappers = new HashMap<>();

        wrappers.put(start.getNode(), start);
        wrappers.put(target.getNode(), target);

        openSet.add(start);
    }

    private void findPath() {
        while (!openSet.isEmpty()) {
            NodeWrapper node = openSet.poll();
            closedSet.add(node);

            if (node.equals(target)) {
                return;
            }

            if (node.isTraversable()) {
                for (NodeWrapper neighbour : node.neighbours(wrappers)) {
                    if (closedSet.contains(neighbour))
                        continue;

                    int tentativeGScore = neighbour.gCost + distance(node, neighbour);

                    if (tentativeGScore < neighbour.gCost || !openSet.contains(neighbour)) {
                        neighbour.gCost = tentativeGScore;
                        neighbour.hCost = distance(neighbour, target);
                        neighbour.parent = node;

                        if (!openSet.contains(neighbour))
                            openSet.add(neighbour);
                    }
                }
            }
        }
    }

    private int distance(NodeWrapper a, NodeWrapper b) {
        return a.pos().manhattanDistance(b.pos());
    }

    private List<Direction> retraceOffsets() {
        List<Direction> offsets = new ArrayList<>();
        NodeWrapper node = target;

        while (node != null && !node.equals(start)) {
            NodeWrapper parent = node.parent;
            if (parent == null)
                return Collections.emptyList();

            offsets.add(MathUtils.getDirectionFromPos(parent.pos(), node.pos()));
            node = parent;
        }

        Collections.reverse(offsets);

        return offsets;
    }

    private List<BlockPos> retracePath() {
        List<BlockPos> path = new ArrayList<>();
        NodeWrapper node = target;

        while (node != null && !node.equals(start)) {
            path.add(node.pos());
            node = node.parent;
        }

        Collections.reverse(path);

        return path;
    }

    private int calculateTime() {
        int time = 0;
        NodeWrapper node = target;

        while (node != null && !node.equals(start)) {
            node = node.parent;
            time += node.getTime();
        }

        return time;
    }

    private List<NodeWrapper> retrace() {
        List<NodeWrapper> path = new ArrayList<>();
        NodeWrapper node = target;

        while (node != null && !node.equals(start)) {
            path.add(node);
            node = node.parent;
        }

        Collections.reverse(path);

        return path;
    }

    public static List<NodeWrapper> run(INode origin, INode target) {
        Objects.requireNonNull(origin, "origin is null");
        Objects.requireNonNull(target, "target is null");

        Pathfinder pathfinder = new Pathfinder(new NodeWrapper(origin), new NodeWrapper(target));
        pathfinder.findPath();

        return pathfinder.retrace();
    }

    public static List<BlockPos> findPath(INode origin, INode target) {
        Objects.requireNonNull(target, "target is null");

        Pathfinder pathfinder = new Pathfinder(new NodeWrapper(origin), new NodeWrapper(target));
        pathfinder.findPath();

        return pathfinder.retracePath();
    }

    public static List<Direction> findOffsets(INode origin, INode target) {
        Pathfinder pathfinder = new Pathfinder(new NodeWrapper(origin), new NodeWrapper(target));
        pathfinder.findPath();

        return pathfinder.retraceOffsets();
    }

    public static int findTime(INode origin, IEndpointNode target) {
        Pathfinder pathfinder = new Pathfinder(new NodeWrapper(origin), new NodeWrapper(target));
        pathfinder.findPath();

        return pathfinder.calculateTime();
    }

    // public static boolean pathExists() {}
}
