package io.github.ramboxeu.techworks.common;

import io.github.ramboxeu.techworks.common.registry.ItemRegistryObject;
import net.minecraft.item.Item;

public class Utils {
    public static Item[] unpackItemObjectsArray(ItemRegistryObject<?>[] objects) {
        Item[] items = new Item[objects.length];

        for (int i = 0; i < objects.length; i++) {
            items[i] = objects[i].getItem();
        }

        return items;
    }
}
