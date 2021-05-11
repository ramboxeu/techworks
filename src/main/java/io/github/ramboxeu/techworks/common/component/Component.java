package io.github.ramboxeu.techworks.common.component;

import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.*;

import java.util.Collections;
import java.util.List;

public abstract class Component implements IItemProvider {

    public static final Style TOOLTIP_STYLE = Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.DARK_GRAY));

    private final ComponentType<?> type;
    protected final ResourceLocation id;
    protected final Item item;

    private String translationKey;
    private List<ITextComponent> tooltip;
    private ITextComponent name;

    public Component(ComponentType<?> type, ResourceLocation id, Item item) {
        this.type = type;
        this.id = id;
        this.item = item;
    }

    @Override
    public Item asItem() {
        return item;
    }

    public ComponentType<?> getType() {
        return type;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public boolean isBase() {
        return this.id.equals(type.getBaseComponentId());
    }

    public ITextComponent getName() {
        if (name == null) {
            name = new TranslationTextComponent(getTranslationKey());
        }

        return name;
    }

    public String getTranslationKey() {
        return getDefaultTranslationKey();
    }

    public List<ITextComponent> getTooltipInfo() {
        if (tooltip == null) {
            tooltip = collectTooltip();
        }

        return tooltip;
    }

    protected List<ITextComponent> collectTooltip() {
        return Collections.emptyList();
    }

    private String getDefaultTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.makeTranslationKey("component", id);
        }

        return translationKey;
    }
}
