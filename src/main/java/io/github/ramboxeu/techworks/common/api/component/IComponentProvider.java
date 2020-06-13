package io.github.ramboxeu.techworks.common.api.component;

/**
 * Classes implement this to signify they are able to provide components. It's up to the user what they will use for
 * the component container and storage of them.
 */
public interface IComponentProvider {
    /**
     * Creates new instance of component this provider provides.
     *
     * @param list The list component will be attached to
     * @param <T> The type of list's container
     * @return New instance of component
     */
    <T> IComponent create(IComponentList<T> list);
}
