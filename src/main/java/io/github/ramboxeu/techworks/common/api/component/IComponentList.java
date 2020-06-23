package io.github.ramboxeu.techworks.common.api.component;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface IComponentList {
//    /**
//     * Adds a IComponent to the list. List can only hold one component of given type.
//     * @param component component to add
//     * @return true if component has been added
//     */
//    boolean add(IComponent component);
//
//    /**
//     * Removes a IComponent form list.
//     * @param component component to be removed
//     * @return true if component has been removed
//     */
//    boolean remove(IComponent component);

    /**
     * Return container for this list. It can be anything: Item, BlockEntity, Entity, etc.
     * @return container of this list
     */
    Object getContainer();

//    /**
//     * Retrieves item form list
//     * @param type type of component to return
//     * @return component of given type
//     */
//    @Nullable
//    IComponent getComponent(ComponentType type);
//
//    /**
//     * Checks if list has a given type
//     * @param type type to check for
//     * @return true if type was found
//     */
//    boolean hasComponent(@NotNull ComponentType type);
//
//    /**
//     * Ticks whole list
//     */
//    void tick();

    /**
     * Returns a stream of this list.
     * @return stream of this list
     */
    Stream<IComponent> stream();

    /**
     * Shorthand to find component that matches given predicate. If multiple ones are matched returns only first one.
     * @param predicate the predicate to test
     * @return the first of components that matched
     */
    Optional<IComponent> find(Predicate<IComponent> predicate);
}
