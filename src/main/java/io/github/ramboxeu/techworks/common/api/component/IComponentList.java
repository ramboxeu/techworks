package io.github.ramboxeu.techworks.common.api.component;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public interface IComponentList<T> {
    /**
     * Adds a IComponent to the list. List can only hold one component of given type.
     * @param component component to add
     * @return true if component has been added
     */
    boolean add(IComponent component);

    /**
     * Removes a IComponent form list.
     * @param component component to be removed
     * @return true if component has been removed
     */
    boolean remove(IComponent component);

    /**
     * Return container for this list. It can be anything: Item, BlockEntity, Entity, etc.
     * @return container of this list
     */
    T getContainer();

    /**
     * Retrieves item form list
     * @param type type of component to return
     * @return component of given type
     */
    @Nullable
    IComponent getComponent(ComponentType type);

    /**
     * Checks if list has a given type
     * @param type type to check for
     * @return true if type was found
     */
    boolean hasComponent(@NotNull ComponentType type);

    /**
     * Ticks whole list
     */
    void tick();
}