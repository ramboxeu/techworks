package io.github.ramboxeu.techworks.client.container.sync;

public interface IExtendedContainerListener {
    void sendObjectUpdate(int holderId, ObjectHolder<?> holder);
}
