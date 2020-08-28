package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.tag.TechworksItemTags;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

/*
 * Data driven crafting, with functions in json
 * Limitations:
 *  - Arrays don't work
 *  - Saved ingredients don't remember the stack count (only item name)
 *  - Copy path can only be 1 level deep
 *  - Casing not being taken into account when editing a machine (by putting it in main slot) is hardcoded
 */
@MethodsReturnNonnullByDefault
public class MachineAssemblyRecipe implements IRecipe<IInventory> {
    private final ResourceLocation id;
    private final Map<String, ExtendedIngredient> map;
    private final NonNullList<ExtendedIngredient> pattern;
    private final NonNullList<Ingredient> ingredients;
    private final boolean[] tools; // 0 - hammer, 1 - wrench, 2 - screwdriver, 3 - soldering iron
    private final Ingredient mainSlot;
    private final Ingredient secondarySlot;
    private final Result result;

    public MachineAssemblyRecipe(ResourceLocation id, Map<String, ExtendedIngredient> map, NonNullList<ExtendedIngredient> pattern, boolean[] tools, Ingredient mainSlot, Ingredient secondarySlot, Result result) {
        this.id = id;
        this.map = map;
        this.pattern = pattern;
        this.tools = tools;
        this.mainSlot = mainSlot;
        this.secondarySlot = secondarySlot;
        this.result = result;

        ingredients = NonNullList.withSize(pattern.size(), Ingredient.EMPTY);

        for (int i = 0; i < ingredients.size(); i++) {
            ingredients.set(i, pattern.get(i).getIngredient());
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(IInventory inv, World world) {
        if (inv.getSizeInventory() != 15) {
            return false;
        }

        ItemStack mainStack = inv.getStackInSlot(0);
        if (!mainSlot.test(mainStack)) {
            return false;
        }

        if (!mainStack.getItem().isIn(TechworksItemTags.MACHINES) && !secondarySlot.test(inv.getStackInSlot(1))) {
            return false;
        }

        if (tools[0] && inv.getStackInSlot(2).isEmpty()) {
            return false;
        }

        if (tools[1] && inv.getStackInSlot(3).isEmpty()) {
            return false;
        }

        if (tools[2] && inv.getStackInSlot(4).isEmpty()) {
            return false;
        }

        if (tools[3] && inv.getStackInSlot(5).isEmpty()) {
            return false;
        }

        boolean flag = true;

        for (int i = 0; i < 9; i++) {
            flag = flag && pattern.get(i).test(inv.getStackInSlot(i + 6));
        }

        return flag;
    }

    public boolean[] getTools() {
        return tools;
    }

    @Override // Safe to modify afterwards
    public ItemStack getCraftingResult(@Nonnull IInventory inv) {
        Map<String, ItemStack> stackMap = null;
        List<Item> savedIngredients = Collections.emptyList();

        if (map != null) {
             stackMap = new HashMap<>(map.size());
             savedIngredients = new ArrayList<>(map.size());

            for (Map.Entry<String, ExtendedIngredient> entry : map.entrySet()) {
                boolean flag = false;

                for (int i = 0; i < 9; i++) {
                    ItemStack stack = inv.getStackInSlot(i + 6);
                    ExtendedIngredient ingredient = entry.getValue();
                    if (ingredient.test(stack)) {
                        if (!flag) {
                            stackMap.put(entry.getKey(), stack);
                            flag = true;
                        }

                        if (ingredient.shouldSave()) {
                            savedIngredients.add(stack.getItem());
                        }
                    }
                }
            }
        }

        ItemStack resultStack = result.craft(stackMap);

        if (!savedIngredients.isEmpty()) {
            CompoundNBT nbt = resultStack.getOrCreateTag();
            ListNBT list = new ListNBT();

            for (int i = 0; i < savedIngredients.size(); i++) {
                list.add(i, StringNBT.valueOf(savedIngredients.get(i).getRegistryName().toString()));
            }

            nbt.put("Ingredients", list);
        }

        return resultStack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width == 3 && height == 3;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result.getRawOutput();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return Registration.MACHINE_ASSEMBLY_RECIPE_SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return Registration.MACHINE_ASSEMBLY_RECIPE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @MethodsReturnNonnullByDefault
    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MachineAssemblyRecipe> {
        public Serializer() {
            setRegistryName(new ResourceLocation(Techworks.MOD_ID, "machine_assembly"));
        }

        @Override
        @ParametersAreNonnullByDefault
        public MachineAssemblyRecipe read(ResourceLocation recipeId, JsonObject json) {
            Map<String, ExtendedIngredient> key = getKey(json);
            NonNullList<ExtendedIngredient> ingredients = getPattern(json, key);
            boolean[] tools = getTools(json);
            Ingredient blueprint = Ingredient.deserialize(json.get("main_slot"));
            Ingredient casing = Ingredient.deserialize(json.get("secondary_slot"));
            Result result = Result.deserialize(JSONUtils.getJsonObject(json, "result"));

            return new MachineAssemblyRecipe(recipeId, key, ingredients, tools, blueprint, casing, result);
        }

        @Nullable
        @Override
        @ParametersAreNonnullByDefault
        public MachineAssemblyRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            int patternSize = buffer.readByte();

            NonNullList<ExtendedIngredient> pattern = NonNullList.withSize(patternSize, ExtendedIngredient.EMPTY);
            for (int i = 0; i < patternSize; i++) {
                pattern.set(i, ExtendedIngredient.read(buffer));
            }

            boolean[] tools = new boolean[4];
            for (int i = 0; i < 4; i++) {
                tools[i] = buffer.readBoolean();
            }

            Ingredient blueprint = Ingredient.read(buffer);
            Ingredient casing = Ingredient.read(buffer);
            Result result = Result.read(buffer);

            return new MachineAssemblyRecipe(recipeId, null, pattern, tools, blueprint, casing, result);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void write(PacketBuffer buffer, MachineAssemblyRecipe recipe) {
            buffer.writeByte(recipe.pattern.size());

            for (ExtendedIngredient ingredient : recipe.pattern) {
                ingredient.write(buffer);
            }

            for (boolean tool : recipe.tools) {
                buffer.writeBoolean(tool);
            }

            recipe.mainSlot.write(buffer);
            recipe.secondarySlot.write(buffer);

            recipe.result.write(buffer);
        }

        public static Map<String, ExtendedIngredient> getKey(JsonObject json) {
            JsonObject keyJson = JSONUtils.getJsonObject(json, "key");
            HashMap<String, ExtendedIngredient> keyMap = new HashMap<>();

            for (Map.Entry<String, JsonElement> key : keyJson.entrySet()) {
                if (key.getKey().length() != 1) {
                    throw new JsonSyntaxException("Invalid key: '" + key.getKey() + "' is longer than 1 character");
                }

                if (" ".equals(key.getKey())) {
                    throw new JsonSyntaxException("Invalid key: ' ' is a reserved symbol");
                }

                keyMap.put(key.getKey(), ExtendedIngredient.deserialize(key.getValue()));
            }

            return keyMap;
        }

        public static NonNullList<ExtendedIngredient> getPattern(JsonObject json, Map<String, ExtendedIngredient> keyMap) {
            JsonArray patternJson = JSONUtils.getJsonArray(json, "pattern");

            Set<String> keySet = new HashSet<>(keyMap.keySet());
            NonNullList<ExtendedIngredient> ingredients = NonNullList.withSize(9, ExtendedIngredient.EMPTY);

            if (patternJson.size() - 3 != 0) {
                throw new JsonSyntaxException("Invalid pattern: wrong row count, must be 3");
            }

            for (int i = 0; i < 3; i++) {
                String row = JSONUtils.getString(patternJson.get(i), "pattern[" + i + "]");

                if (row.length() - 3 != 0) {
                    throw new JsonSyntaxException("Invalid pattern: wrong column count, must be 3");
                }

                for (int j = 0; j < 3; j++) {
                    String key = row.substring(j, j + 1);
                    ExtendedIngredient ingredient = key.equals(" ") ? ExtendedIngredient.EMPTY : keyMap.get(key);

                    if (ingredient == null) {
                        throw new JsonSyntaxException("Pattern error: symbol '" + key + "' is used, but it isn't defined in keys");
                    }

                    keySet.remove(key);
                    ingredients.set(j + 3 * i, ingredient);
                }
            }

            if (!keySet.isEmpty()) {
                throw new JsonSyntaxException("Pattern error: symbols: " + keySet + " are defined but unused");
            }

            return ingredients;
        }

        public static boolean[] getTools(JsonObject json) {
            JsonObject toolsJson = JSONUtils.getJsonObject(json, "tools");

            return new boolean[]{
                    JSONUtils.getBoolean(toolsJson, "hammer", false),
                    JSONUtils.getBoolean(toolsJson, "wrench", false),
                    JSONUtils.getBoolean(toolsJson, "screwdriver", false),
                    JSONUtils.getBoolean(toolsJson, "soldering_iron", false),
            };
        }
    }
}
