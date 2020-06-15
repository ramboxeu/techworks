package io.github.ramboxeu.techworks.common.component;

import io.github.ramboxeu.techworks.common.api.component.IComponentList;
import io.github.ramboxeu.techworks.common.api.component.IComponentProvider;
import io.github.ramboxeu.techworks.common.registry.ComponentProviders;
import net.minecraft.nbt.CompoundTag;

public class BasicBoilingComponent extends BaseBoilingComponent {
    private int intDummy = 10;
    //private String stringDummy = "this is dumb string";

    public BasicBoilingComponent(IComponentList<?> componentList) {
        super(componentList);
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public IComponentProvider getProvider() {
        return ComponentProviders.BASIC_BOILING_COMPONENT;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt("IntDummy", intDummy);
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        intDummy = tag.getInt("IntDummy");
    }

    @Override
    public void tick() {
        intDummy++;
        super.tick();
    }
}
