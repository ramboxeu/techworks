package io.github.ramboxeu.techworks.common.api.component;

import com.sun.istack.internal.NotNull;

public interface IComponent {
    /**
     * Gets type of this component
     * @return type of this component
     */
    @NotNull
    ComponentType getType();

    /**
     * Gets level og this component. This is used to determine how advanced component is (higher is better).
     * @return level of this component
     */
    int getLevel();

    /**
     * Sets list this component belongs to
     * @param list list this component is in
     */
    void setList(@NotNull IComponentList<?> list);

    /**
     * Ticks the component
     */
    void tick();
}
