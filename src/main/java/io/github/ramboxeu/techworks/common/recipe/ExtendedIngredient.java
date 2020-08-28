package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;

public class ExtendedIngredient {
    public static final ExtendedIngredient EMPTY = new ExtendedIngredient(Ingredient.EMPTY, false);

    private final Ingredient ingredient;
    private final boolean save;

    public ExtendedIngredient(Ingredient ingredient, boolean save) {
        this.ingredient = ingredient;
        this.save = save;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public boolean shouldSave() {
        return save;
    }

    public boolean test(ItemStack stack) {
        return ingredient.test(stack);
    }

    public void write(PacketBuffer buffer) {
        buffer.writeBoolean(save);
        ingredient.write(buffer);
    }

    public static ExtendedIngredient deserialize(JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            if (json.isJsonObject() && json.getAsJsonObject().has("save")) {
                return new ExtendedIngredient(Ingredient.deserialize(json), json.getAsJsonObject().get("save").getAsBoolean());
            } else {
                return new ExtendedIngredient(Ingredient.deserialize(json), false);
            }
        } else {
            throw new JsonSyntaxException("Invalid ingredient: object cannot be null");
        }
    }

    public static ExtendedIngredient read(PacketBuffer buffer) {
        return new ExtendedIngredient(Ingredient.read(buffer), buffer.readBoolean());
    }
}
