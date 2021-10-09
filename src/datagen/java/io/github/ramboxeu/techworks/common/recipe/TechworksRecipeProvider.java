package io.github.ramboxeu.techworks.common.recipe;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksItems;
import io.github.ramboxeu.techworks.common.registry.IItemSupplier;
import io.github.ramboxeu.techworks.common.registry.ItemRegistryObject;
import io.github.ramboxeu.techworks.common.tag.TechworksItemTags;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.function.Consumer;

public class TechworksRecipeProvider extends RecipeProvider {

    public TechworksRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        // Grinder
        dustFromIngot(consumer, TechworksItemTags.COPPER_INGOTS, TechworksItems.COPPER_DUST);
        dustFromIngot(consumer, TechworksItemTags.LITHIUM_INGOTS, TechworksItems.LITHIUM_DUST);
        dustFromIngot(consumer, Tags.Items.INGOTS_IRON, TechworksItems.IRON_DUST);
        dustFromIngot(consumer, Tags.Items.INGOTS_GOLD, TechworksItems.GOLD_DUST);

        dustFromOre(consumer, TechworksItemTags.COPPER_ORES, TechworksItems.COPPER_DUST);
        dustFromOre(consumer, TechworksItemTags.LITHIUM_ORES, TechworksItems.LITHIUM_DUST);
        dustFromOre(consumer, Tags.Items.ORES_IRON, TechworksItems.IRON_DUST);
        dustFromOre(consumer, Tags.Items.ORES_GOLD, TechworksItems.GOLD_DUST);

        crushedOre(consumer, TechworksItemTags.COPPER_ORES, TechworksItems.CRUSHED_COPPER_ORE);
        crushedOre(consumer, TechworksItemTags.LITHIUM_ORES, TechworksItems.CRUSHED_LITHIUM_ORE);
        crushedOre(consumer, Tags.Items.ORES_IRON, TechworksItems.CRUSHED_IRON_ORE);
        crushedOre(consumer, Tags.Items.ORES_GOLD, TechworksItems.CRUSHED_GOLD_ORE);

        // Smelting
        ingotFromOre(consumer, TechworksItemTags.COPPER_ORES, TechworksItems.COPPER_INGOT, "copper_ore");
        ingotFromOre(consumer, TechworksItemTags.LITHIUM_ORES, TechworksItems.LITHIUM_INGOT, "lithium_ore");

        ingotFromDust(consumer, TechworksItemTags.COPPER_DUSTS, TechworksItems.COPPER_INGOT, "copper_dust");
        ingotFromDust(consumer, TechworksItemTags.LITHIUM_DUSTS, TechworksItems.LITHIUM_INGOT, "lithium_dust");
        ingotFromDust(consumer, TechworksItemTags.IRON_DUSTS, Items.IRON_INGOT, "iron_dust");
        ingotFromDust(consumer, TechworksItemTags.GOLD_DUSTS, Items.GOLD_INGOT, "gold_dust");

        ingotFromCrushedOre(consumer, TechworksItems.CRUSHED_COPPER_ORE, TechworksItems.COPPER_INGOT);
        ingotFromCrushedOre(consumer, TechworksItems.CRUSHED_LITHIUM_ORE, TechworksItems.LITHIUM_INGOT);
        ingotFromCrushedOre(consumer, TechworksItems.CRUSHED_IRON_ORE, Items.IRON_INGOT);
        ingotFromCrushedOre(consumer, TechworksItems.CRUSHED_GOLD_ORE, Items.GOLD_INGOT);

        // Ore Washing
        oreWashing(consumer, TechworksItems.CRUSHED_COPPER_ORE, oreWashingResult(TechworksItems.COPPER_DUST).count(2, 3), oreWashingResult(Items.GRAVEL).count(1));
        oreWashing(consumer, TechworksItems.CRUSHED_LITHIUM_ORE, oreWashingResult(TechworksItems.LITHIUM_DUST).count(2, 3), oreWashingResult(Items.GRAVEL).count(1));
        oreWashing(consumer, TechworksItems.CRUSHED_IRON_ORE, oreWashingResult(TechworksItems.IRON_DUST).count(2, 3), oreWashingResult(Items.GRAVEL).count(1));
        oreWashing(consumer, TechworksItems.CRUSHED_GOLD_ORE, oreWashingResult(TechworksItems.GOLD_DUST).count(2, 3), oreWashingResult(Items.GRAVEL).count(1));
    }

    @Override
    public String getName() {
        return "Recipes: techworks";
    }

    private static void dustFromIngot(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> ingotTag, ItemRegistryObject<?> dust) {
        GrinderRecipeBuilder.grinding(ingredient(ingotTag), result(dust), 1500).build(consumer, "grinder/" + dust.getId().getPath() + "_from_ingot");
    }

    private static void dustFromOre(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> oreTag, ItemRegistryObject<?> dust) {
        GrinderRecipeBuilder.grinding(ingredient(oreTag), result(dust), 2000).build(consumer, "grinder/" + dust.getId().getPath() + "_from_ore");
    }

    public static void crushedOre(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> oreTag, ItemRegistryObject<?> crushedOre) {
        GrinderRecipeBuilder.oreCrushing(ingredient(oreTag), result(crushedOre, 2), 2000).build(consumer, "grinder/" + crushedOre.getId().getPath());
    }

    public static void ingotFromDust(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> dustTag, ItemRegistryObject<?> ingot, String criterionItemName) {
        ingotFromDust(consumer, dustTag, result(ingot), criterionItemName, ingot.getId().getPath());
    }

    public static void ingotFromDust(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> dustTag, Item ingot, String criterionItemName) {
        ingotFromDust(consumer, dustTag, () -> ingot, criterionItemName, name(ingot));
    }

    public static void ingotFromDust(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> dustTag, IRecipeResult result, String criterionItemName, String name) {
        SmeltingRecipeBuilder.smelting(ingredient(dustTag), result, 0.35f, 1500).build(consumer, "furnace/electric/" + name + "_from_dust");
        CookingRecipeBuilder.smeltingRecipe(ingredient(dustTag), result.getItem(), 0.35f, 150).addCriterion("has_" + criterionItemName, hasItem(dustTag)).build(consumer, modLoc("furnace/vanilla/" + name + "_from_dust"));
        CookingRecipeBuilder.blastingRecipe(ingredient(dustTag), result.getItem(), 0.35f, 75).addCriterion("has_" + criterionItemName, hasItem(dustTag)).build(consumer, modLoc("furnace/blast/" + name + "_from_dust"));
    }

    public static void ingotFromOre(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> oreTag, ItemRegistryObject<?> ingot, String criterionItemName) {
        SmeltingRecipeBuilder.smelting(ingredient(oreTag), result(ingot), 0.7f, 2000).build(consumer, "furnace/electric/" + ingot.getId().getPath() + "_from_ore");
        CookingRecipeBuilder.smeltingRecipe(ingredient(oreTag), ingot.get(), 0.7f, 200).addCriterion("has_" + criterionItemName, hasItem(oreTag)).build(consumer, modLoc("furnace/vanilla/" + ingot.getId().getPath() + "_from_ore"));
        CookingRecipeBuilder.blastingRecipe(ingredient(oreTag), ingot.get(), 0.7f, 100).addCriterion("has_" + criterionItemName, hasItem(oreTag)).build(consumer, modLoc("furnace/blast/" + ingot.getId().getPath() + "_from_ore"));
    }

    public static void ingotFromCrushedOre(Consumer<IFinishedRecipe> consumer, ItemRegistryObject<?> crushedOre, ItemRegistryObject<?> ingot) {
        SmeltingRecipeBuilder.smelting(ingredient(crushedOre), result(ingot), 0.6f, 1500).build(consumer, "furnace/electric/" + ingot.getId().getPath() + "_from_crushed_ore");
    }

    public static void ingotFromCrushedOre(Consumer<IFinishedRecipe> consumer, ItemRegistryObject<?> crushedOre, Item ingot) {
        SmeltingRecipeBuilder.smelting(ingredient(crushedOre), () -> ingot, 0.6f, 1500).build(consumer, "furnace/electric/" + name(ingot) + "_from_crushed_ore");
    }

    public static void oreWashing(Consumer<IFinishedRecipe> consumer, ItemRegistryObject<?> crushedOre, OreWashingRecipeBuilder.ResultBuilder... results) {
        if (results.length == 0) throw new IllegalArgumentException("Empty results");

        Item primary = results[0].getResult().getItem();
        new OreWashingRecipeBuilder(ingredient(crushedOre), Arrays.asList(results)).build(consumer, "ore_washer/" + name(primary));
    }

    public static OreWashingRecipeBuilder.ResultBuilder oreWashingResult(IItemSupplier supplier) {
        return new OreWashingRecipeBuilder.ResultBuilder(supplier::getAsItem);
    }

    public static OreWashingRecipeBuilder.ResultBuilder oreWashingResult(IItemProvider provider) {
        return new OreWashingRecipeBuilder.ResultBuilder(provider::asItem);
    }

    private static Ingredient ingredient(ITag.INamedTag<Item> tag) {
        return Ingredient.fromTag(tag);
    }

    private static Ingredient ingredient(IItemSupplier tag) {
        return Ingredient.fromItems(tag.getAsItem());
    }

    private static IRecipeResult result(IItemSupplier item) {
        return new RecipeResult(item, 1);
    }

    private static IRecipeResult result(IItemSupplier item, int count) {
        return new RecipeResult(item, count);
    }

    private static ICriterionInstance hasItem(ITag.INamedTag<Item> tag) {
        ItemPredicate[] predicates = new ItemPredicate[] { ItemPredicate.Builder.create().tag(tag).build() };
        return new InventoryChangeTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, predicates);
    }

    private static String name(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }

    private static ResourceLocation modLoc(String name) {
        return new ResourceLocation(Techworks.MOD_ID, name);
    }
}
