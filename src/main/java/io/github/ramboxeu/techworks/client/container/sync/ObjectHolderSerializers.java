package io.github.ramboxeu.techworks.client.container.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public final class ObjectHolderSerializers {
    private static final List<ObjectHolderSerializer<?>> SERIALIZERS = new ArrayList<>();

    public static final FluidStackHolder.Serializer FLUID_STACK = add(FluidStackHolder.Serializer::new);

    public static ObjectHolderSerializer<?> getSerializer(int serializerId) {
        return SERIALIZERS.get(serializerId);
    }

    private static <T extends ObjectHolderSerializer<?>> T add(IntFunction<T> factory) {
        T serializer = factory.apply(SERIALIZERS.size());
        SERIALIZERS.add(serializer);
        return serializer;
    }
}
