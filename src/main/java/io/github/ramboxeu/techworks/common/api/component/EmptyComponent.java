package io.github.ramboxeu.techworks.common.api.component;

public class EmptyComponent implements IComponent {
    private EmptyComponent() {}

    public static final EmptyComponent INSTANCE = new EmptyComponent();

    @Override
    public ComponentType getType() {
        return null;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public IComponentProvider getProvider() {
        return null;
    }

    @Override
    public void tick() {}
}
