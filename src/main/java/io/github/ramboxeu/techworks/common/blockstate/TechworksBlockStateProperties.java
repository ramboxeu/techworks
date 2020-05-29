package io.github.ramboxeu.techworks.common.blockstate;

import io.github.ramboxeu.techworks.common.util.TechworksUtils;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;

public class TechworksBlockStateProperties {
    public static final BooleanProperty RUNNING = BooleanProperty.of("running");
    public static final DirectionProperty FACING = DirectionProperty.of("facing", TechworksUtils.HORIZONTAL_DIRECTIONS);
}
