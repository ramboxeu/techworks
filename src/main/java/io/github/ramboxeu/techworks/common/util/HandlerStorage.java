package io.github.ramboxeu.techworks.common.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class HandlerStorage<T> {
    private final Map<Direction, Entry<T>> handlers = new EnumMap<>(Direction.class);
    private final Capability<T> capability;

    public HandlerStorage(Capability<T> capability) {
        this.capability = capability;
    }

    public boolean isEmpty() {
        return handlers.isEmpty();
    }

    @Nullable
    public T store(Direction side, @Nonnull TileEntity tile) {
        LazyOptional<T> holder = tile.getCapability(capability, side.getOpposite());

        if (holder.isPresent()) {
            T handler = holder.orElseThrow(() -> new RuntimeException("Present holder without value"));
            handlers.put(side, new Entry<>(handler, holder, tile, side));
            subscribe(side, holder);
            return handler;
        }

        return null;
    }

    @Nullable
    public T remove(Direction side) {
        if (handlers.containsKey(side))
            return handlers.remove(side).handler;

        return null;
    }

    @Nullable
    public T get(Direction side) {
        return handlers.get(side).handler;
    }

    public void forEach(BiConsumer<Direction, T> consumer) {
        handlers.forEach((side, entry) -> consumer.accept(side, entry.handler));
    }

    public void forEachEntry(BiConsumer<Direction, Entry<T>> consumer) {
        handlers.forEach(consumer);
    }

    public Collection<Entry<T>> entries() {
        return handlers.values();
    }

    private void subscribe(Direction side, LazyOptional<T> holder) {
        holder.addListener(h -> handlers.remove(side));
    }

    public Stream<T> stream() {
        return handlers.values().stream().map(Entry::getHandler);
    }

    public static class Entry<T> {
        private final T handler;
        private final LazyOptional<T> holder;
        private final TileEntity tile;
        private final Direction side;

        public Entry(T handler, LazyOptional<T> holder, TileEntity tile, Direction side) {
            this.handler = handler;
            this.holder = holder;
            this.tile = tile;
            this.side = side;
        }

        public T getHandler() {
            return handler;
        }

        public LazyOptional<T> getHolder() {
            return holder;
        }

        public TileEntity getTile() {
            return tile;
        }

        public Direction getSide() {
            return side;
        }
    }
}
