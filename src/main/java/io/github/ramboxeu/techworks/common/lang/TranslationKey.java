package io.github.ramboxeu.techworks.common.lang;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registry.ContainerRegistryObject;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;

public final class TranslationKey {
    private final String key;

    private TranslationKey(String key) {
        this.key = key;
    }

    public String get() {
        return key;
    }

    public TranslationTextComponent text() {
        return new TranslationTextComponent(key);
    }

    public TranslationTextComponent text(Object... args) {
        return new TranslationTextComponent(key, args);
    }

    public TranslationTextComponent styledText(Style style) {
        return (TranslationTextComponent) new TranslationTextComponent(key).setStyle(style);
    }

    public TranslationTextComponent styledText(Style style, Object... args) {
        return (TranslationTextComponent) new TranslationTextComponent(key, args).setStyle(style);
    }

    public static TranslationKey container(ContainerRegistryObject<?> container) {
        return make("container", container.getId().getPath());
    }

    public static TranslationKey widget(String widget) {
        return make("widget", widget);
    }

    public static TranslationKey widget(String widget, String name) {
        return make("widget", widget, name);
    }

    public static TranslationKey tooltip(String name) {
        return make("tooltip", name);
    }

    public static TranslationKey fluid(String name) {
        return make("fluid", name);
    }

    public static TranslationKey text(String name) {
        return make("text", name);
    }

    public static TranslationKey component(String name) {
        return make("component", name);
    }

    public static TranslationKey status(String name) {
        return make("status", name);
    }

    public static TranslationKey make(String type, String name) {
        return new TranslationKey(type + '.' + Techworks.MOD_ID + '.' + name);
    }

    public static TranslationKey make(String type, String name, String more) {
        return new TranslationKey(type + '.' + Techworks.MOD_ID + '.' + name + '.' + more);
    }
}
