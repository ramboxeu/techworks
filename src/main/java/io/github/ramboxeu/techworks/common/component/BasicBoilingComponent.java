package io.github.ramboxeu.techworks.common.component;

import io.github.ramboxeu.techworks.common.api.component.IComponentList;
import io.github.ramboxeu.techworks.common.api.component.IComponentProvider;
import net.minecraft.nbt.CompoundTag;

public class BasicBoilingComponent extends BaseBoilingComponent {
    private int intDummy = 10;
    //private String stringDummy = "this is dumb string";

    public BasicBoilingComponent(IComponentList<?> componentList, IComponentProvider provider) {
        super(componentList, provider);
    }

    @Override
    public int getLevel() {
        return 1;
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
