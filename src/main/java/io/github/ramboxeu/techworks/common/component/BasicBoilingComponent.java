package io.github.ramboxeu.techworks.common.component;

import io.github.ramboxeu.techworks.common.api.component.ComponentType;
import io.github.ramboxeu.techworks.common.api.component.IComponent;
import io.github.ramboxeu.techworks.common.api.component.IComponentList;
import io.github.ramboxeu.techworks.common.api.component.IComponentProvider;
import io.github.ramboxeu.techworks.common.registry.ComponentProviders;
import io.github.ramboxeu.techworks.common.registry.ComponentTypes;
import net.minecraft.nbt.CompoundTag;

public class BasicBoilingComponent implements IComponent {
    private int intDummy = 10;
    //private String stringDummy = "this is dumb string";
    private IComponentList componentList;
//    private Inventory inventory = new BasicInventory(1);

    public BasicBoilingComponent(IComponentList componentList) {
        this.componentList = componentList;
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public ComponentType getType() {
        return ComponentTypes.BOILING_COMPONENT;
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
    }
}
