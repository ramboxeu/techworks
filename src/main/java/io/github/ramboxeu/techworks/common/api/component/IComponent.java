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
     * Provides a pointer to provider of this component. This is used to for example keep
     * components in {@link ComponentInventory} in sync with their item providers
     * @return pointer to provider of this component
     */
    IComponentProvider getProvider();

    /**
     * Gets level of this component. This is used to determine how advanced component is (higher is better).
     * @return level of this component
     */
    int getLevel();

//    /**
//     * Sets list this component belongs to
//     * @param list list this component is in
//     * @param <T> type of this lists container
//     */
//    void setList(@NotNull IComponentList list);

    /**
     * Ticks the component
     */
    void tick();
}
