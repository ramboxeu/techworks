package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public class SizedIngredient extends Ingredient {
    private final IItemList[] acceptedItems;
    private final boolean exact;
    private ItemStack[] matchingStacks;
    private IntList packedStacks;

    protected SizedIngredient(Stream<? extends IItemList> acceptedItems) {
        super(Stream.empty());
        this.acceptedItems = acceptedItems.toArray(IItemList[]::new);
        this.exact = false;
    }

    private SizedIngredient(IItemList[] acceptedItems, boolean exact) {
        super(Stream.empty());
        this.acceptedItems = acceptedItems;
        this.exact = exact;
    }

    private SizedIngredient(IItemList acceptedItem, boolean exact) {
        super(Stream.empty());
        this.acceptedItems = new  IItemList[] { acceptedItem };
        this.exact = exact;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack == null)
            return false;

        computeMatchingStacks();

        if (matchingStacks.length == 0)
            return stack.isEmpty();

        for (ItemStack example : matchingStacks) {
            if (example.getItem() == stack.getItem() && sizeMatches(stack, example))
                return true;
        }

        return false;
    }

    public int getCount(ItemStack stack) {
        for (ItemStack example : matchingStacks) {
            if (example.getItem() == stack.getItem() && example.getCount() <= stack.getCount()) {
                return example.getCount();
            }
        }

        return -1;
    }

    @Override
    public JsonElement serialize() {
        if (acceptedItems.length == 1) {
            JsonObject obj = acceptedItems[0].serialize();
            if (exact) obj.addProperty("exact", exact);
            return obj;
        }

        JsonArray array = new JsonArray();
        for (IItemList list : acceptedItems) {
            array.add(list.serialize());
        }

        return array;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public IntList getValidItemStacksPacked() {
        if (packedStacks == null) {
            computeMatchingStacks();
            packedStacks = new IntArrayList(matchingStacks.length);

            for (ItemStack stack : matchingStacks) {
                packedStacks.add(RecipeItemHelper.pack(stack));
            }

            packedStacks.sort(IntComparators.NATURAL_COMPARATOR);
        }

        return packedStacks;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        computeMatchingStacks();
        return matchingStacks;
    }

    @Override
    public boolean hasNoMatchingItems() {
        return acceptedItems.length == 0 && (matchingStacks == null || matchingStacks.length == 0) && (packedStacks == null || packedStacks.isEmpty());
    }

    @Override
    protected void invalidate() {
        super.invalidate();

        matchingStacks = null;
        packedStacks = null;
    }

    private void computeMatchingStacks() {
        if (matchingStacks == null) {
            matchingStacks = Arrays.stream(acceptedItems)
                    .distinct()
                    .flatMap(list -> list.getStacks().stream()).toArray(ItemStack[]::new);
        }
    }

    private boolean sizeMatches(ItemStack stack, ItemStack example) {
        return stack.getCount() == example.getCount() || (!exact && stack.getCount() >= example.getCount());
    }

    public static SizedIngredient deserialize(@Nullable JsonElement element) {
        if (element == null || element.isJsonNull()) {
            throw new NullPointerException("SizedIngredient cannot be null");
        }

        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            boolean exact = JSONUtils.getBoolean(obj, "exact", false);
            return new SizedIngredient(deserializeSizedItemList(obj), exact);
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            int size = array.size();
            IItemList[] lists = new IItemList[size];

            for (int i = 0; i < size; i++) {
                JsonElement arrElem = array.get(i);

                if (!arrElem.isJsonObject())
                    throw new JsonSyntaxException("Expected SizedIngredient to be an array of objects");

                lists[i] = deserializeSizedItemList(arrElem.getAsJsonObject());
            }

            return new SizedIngredient(lists, false);
        } else {
            throw new JsonSyntaxException("Expected SizedIngredient to be an object or an array of objects");
        }
    }

    public static SizedIngredient read(PacketBuffer buf) {
        boolean exact = buf.readBoolean();
        int size = buf.readVarInt();
        SizedSingleItemList[] lists = new SizedSingleItemList[size];

        for (int i = 0; i < size; i++) {
            lists[i] = new SizedSingleItemList(buf.readItemStack());
        }

        return new SizedIngredient(lists, exact);
    }

    private static Ingredient.IItemList deserializeSizedItemList(JsonObject obj) {
        if (obj.has("tag") && obj.has("item"))
            throw new JsonSyntaxException("Both tag and item SizedIngredient entries are present");

        int count = JSONUtils.getInt(obj, "count", 1);

        if (count <= 0)
            throw new JsonSyntaxException("Expected 'count' to be greater than 0");

        if (obj.has("tag")) {
            ResourceLocation id = new ResourceLocation(JSONUtils.getString(obj, "tag"));
            ITag<Item> tag = TagCollectionManager.getManager().getItemTags().get(id);

            if (tag == null) {
                throw new JsonSyntaxException("Unknown item tag '" + id + "'");
            }

            return new SizedTagItemList(tag, count);
        } else if (obj.has("item")) {
            ResourceLocation id = new ResourceLocation(JSONUtils.getString(obj, "item"));
            Item item = ForgeRegistries.ITEMS.getValue(id);

            if (item == null) {
                throw new JsonSyntaxException("Unknown item '" + id + "'");
            }

            return new SizedSingleItemList(new ItemStack(item, count));
        } else {
            throw new JsonSyntaxException("Expected SizedIngredient entry to have either a tag or item");
        }
    }

    public static SizedIngredient fromTag(ITag<Item> tag, int count, boolean exact) {
        return new SizedIngredient(new SizedTagItemList(tag, count), exact);
    }

    public static class SizedSingleItemList implements IItemList {
        private final ItemStack example;

        public SizedSingleItemList(ItemStack example) {
            this.example = example;
        }

        @Override
        public Collection<ItemStack> getStacks() {
            return Collections.singleton(example);
        }

        @Override
        public JsonObject serialize() {
            JsonObject obj = new JsonObject();

            obj.addProperty("count", example.getCount());
            obj.addProperty("item", ForgeRegistries.ITEMS.getKey(example.getItem()).toString());

            return obj;
        }
    }

    public static class SizedTagItemList implements IItemList {
        private final ITag<Item> tag;
        private final int count;

        public SizedTagItemList(ITag<Item> tag, int count) {
            this.tag = tag;
            this.count = count;
        }

        @Override
        public Collection<ItemStack> getStacks() {
            List<ItemStack> stacks = new ArrayList<>(tag.getAllElements().size());

            for (Item item : tag.getAllElements()) {
                stacks.add(new ItemStack(item, count));
            }

            if (stacks.isEmpty() && !ForgeConfig.SERVER.treatEmptyTagsAsAir.get()) {
                stacks.add(new ItemStack(Blocks.BARRIER).setDisplayName(new StringTextComponent("Empty Tag: " + TagCollectionManager.getManager().getItemTags().getValidatedIdFromTag(tag))));
            }

            return stacks;
        }

        @Override
        public JsonObject serialize() {
            JsonObject obj = new JsonObject();

            obj.addProperty("tag", TagCollectionManager.getManager().getItemTags().getValidatedIdFromTag(tag).toString());
            obj.addProperty("count", count);

            return obj;
        }
    }

    public static class Serializer implements IIngredientSerializer<SizedIngredient> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public SizedIngredient parse(PacketBuffer buf) {
            return SizedIngredient.read(buf);
        }

        @Override
        public SizedIngredient parse(JsonObject json) {
            return SizedIngredient.deserialize(json);
        }

        @Override
        public void write(PacketBuffer buf, SizedIngredient ingredient) {
            ingredient.computeMatchingStacks();
            buf.writeBoolean(ingredient.exact);
            buf.writeVarInt(ingredient.matchingStacks.length);

            for (int i = 0, size = ingredient.matchingStacks.length; i < size; i++) {
                buf.writeItemStack(ingredient.matchingStacks[i]);
            }
        }
    }
}
