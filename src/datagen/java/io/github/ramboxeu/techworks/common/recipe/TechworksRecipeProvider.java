package io.github.ramboxeu.techworks.common.recipe;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksBlocks;
import io.github.ramboxeu.techworks.common.registration.TechworksItems;
import io.github.ramboxeu.techworks.common.registry.IItemSupplier;
import io.github.ramboxeu.techworks.common.registry.ItemRegistryObject;
import io.github.ramboxeu.techworks.common.tag.TechworksItemTags;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
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

        oreDouble(consumer, Tags.Items.ORES_COAL, Items.COAL);

        itemDouble(consumer, ItemTags.COALS, TechworksItems.COAL_DUST);

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

        // Metal Pressing
        plate(consumer, Tags.Items.INGOTS_IRON, TechworksItems.IRON_PLATE);
        plate(consumer, TechworksItemTags.COPPER_INGOTS, TechworksItems.COPPER_PLATE);
        plate(consumer, TechworksItemTags.LITHIUM_INGOTS, TechworksItems.LITHIUM_PLATE);
        gear(consumer, Tags.Items.INGOTS_IRON, TechworksItems.IRON_GEAR);
        wire(consumer, TechworksItemTags.COPPER_PLATES, TechworksItems.COPPER_WIRE);
        doublePlate(consumer, Tags.Items.INGOTS_IRON, TechworksItems.IRON_DOUBLE_PLATE);

        // Industrial Smelting
        quadIngotFromOre(consumer, Tags.Items.ORES_IRON, Items.IRON_INGOT);
        quadIngotFromOre(consumer, Tags.Items.ORES_GOLD, Items.GOLD_INGOT);
        quadIngotFromOre(consumer, TechworksItemTags.COPPER_ORES, TechworksItems.COPPER_INGOT);
        quadIngotFromOre(consumer, TechworksItemTags.LITHIUM_ORES, TechworksItems.LITHIUM_INGOT);
        alloy2Dusts1Ingot(consumer, TechworksItemTags.COAL_DUSTS, Tags.Items.INGOTS_IRON, TechworksItems.STEEL_INGOT);

        // Hammering
        hammeringPlate(consumer, Tags.Items.INGOTS_IRON, TechworksItems.IRON_PLATE);
        hammeringPlate(consumer, TechworksItemTags.COPPER_INGOTS, TechworksItems.COPPER_PLATE);
        hammeringPlate(consumer, TechworksItemTags.LITHIUM_INGOTS, TechworksItems.LITHIUM_PLATE);
        hammeringDoublePlate(consumer, Tags.Items.INGOTS_IRON, TechworksItems.IRON_DOUBLE_PLATE);

        // Wire Cutting
        wireCutting(consumer, TechworksItemTags.COPPER_PLATES, TechworksItems.COPPER_WIRE);

        // Crafting
        shapedRecipe(result(TechworksBlocks.SOLID_FUEL_BURNER)).addCriterion("has_furnace", hasItem(Items.FURNACE)).patternLine("PPP").patternLine("PFP").patternLine("BBB").key('P', TechworksItemTags.IRON_PLATES).key('F', Items.FURNACE).key('B', Items.BRICKS).build(consumer, modLoc(name(TechworksBlocks.SOLID_FUEL_BURNER.getItem())));
        shapedRecipe(result(TechworksItems.ELECTRIFIED_FURNACE)).addCriterion("has_furnace", hasItem(Items.FURNACE)).patternLine("RPR").patternLine("PFP").patternLine("WPW").key('R', Items.REDSTONE).key('P', TechworksItemTags.IRON_PLATES).key('F', Items.FURNACE).key('W', TechworksItems.COPPER_WIRE.get()).build(consumer, modLoc(name(TechworksBlocks.ELECTRIC_FURNACE.getItem())));
        shapedRecipe(result(TechworksItems.SMALL_LIQUID_TANK)).addCriterion("has_bucket", hasItem(Items.BUCKET)).patternLine("BPB").patternLine("PGP").patternLine("BPB").key('B', Items.BUCKET).key('P', TechworksItemTags.IRON_PLATES).key('G', Tags.Items.GLASS).build(consumer, modLoc(name(TechworksItems.SMALL_LIQUID_TANK.get())));
        shapedRecipe(result(TechworksItems.MEDIUM_LIQUID_TANK)).addCriterion("has_small_liquid_tank", hasItem(TechworksItems.SMALL_LIQUID_TANK)).patternLine("PGP").patternLine("BTB").patternLine("PGP").key('P', TechworksItems.IRON_DOUBLE_PLATE.getAsItem()).key('G', Tags.Items.GLASS).key('B', Items.BUCKET).key('T', TechworksItems.SMALL_LIQUID_TANK.get()).build(consumer, modLoc(name(TechworksItems.MEDIUM_LIQUID_TANK.get())));
        shapedRecipe(result(TechworksItems.SMALL_BATTERY)).addCriterion("has_litium_plate", hasItem(TechworksItems.LITHIUM_PLATE)).patternLine("PWP").patternLine("LRL").patternLine("PWP").key('P', TechworksItemTags.IRON_PLATES).key('W', TechworksItems.COPPER_WIRE.get()).key('R', Blocks.REDSTONE_BLOCK).key('L', TechworksItems.LITHIUM_PLATE.get()).build(consumer, modLoc(name(TechworksItems.SMALL_BATTERY.get())));
        shapedRecipe(result(TechworksItems.MEDIUM_BATTERY)).addCriterion("has_small_battery", hasItem(TechworksItems.SMALL_BATTERY)).patternLine("PWP").patternLine("LBL").patternLine("RWR").key('P', TechworksItems.IRON_DOUBLE_PLATE.get()).key('W', TechworksItems.COPPER_WIRE.get()).key('R', Blocks.REDSTONE_BLOCK).key('B', TechworksItems.SMALL_BATTERY.get()).key('L', TechworksItems.LITHIUM_PLATE.get()).build(consumer, modLoc(name(TechworksItems.MEDIUM_BATTERY.get())));
        shapedRecipe(result(TechworksItems.STEEL_HELMET)).addCriterion("has_steel_ingot", hasItem(TechworksItems.STEEL_INGOT)).patternLine("###").patternLine("# #").key('#', TechworksItems.STEEL_INGOT.get()).build(consumer, modLoc(name(TechworksItems.STEEL_HELMET.get())));
        shapedRecipe(result(TechworksItems.STEEL_CHESTPLATE)).addCriterion("has_steel_ingot", hasItem(TechworksItems.STEEL_INGOT)).patternLine("# #").patternLine("###").patternLine("###").key('#', TechworksItems.STEEL_INGOT.get()).build(consumer, modLoc(name(TechworksItems.STEEL_CHESTPLATE.get())));
        shapedRecipe(result(TechworksItems.STEEL_LEGGINGS)).addCriterion("has_steel_ingot", hasItem(TechworksItems.STEEL_INGOT)).patternLine("###").patternLine("# #").patternLine("# #").key('#', TechworksItems.STEEL_INGOT.get()).build(consumer, modLoc(name(TechworksItems.STEEL_LEGGINGS.get())));
        shapedRecipe(result(TechworksItems.STEEL_BOOTS)).addCriterion("has_steel_ingot", hasItem(TechworksItems.STEEL_INGOT)).patternLine("# #").patternLine("# #").key('#', TechworksItems.STEEL_INGOT.get()).build(consumer, modLoc(name(TechworksItems.STEEL_BOOTS.get())));
        shapedRecipe(result(TechworksItems.STEEL_PICKAXE)).addCriterion("has_steel_ingot", hasItem(TechworksItems.STEEL_INGOT)).patternLine("###").patternLine(" X ").patternLine(" X ").key('#', TechworksItems.STEEL_INGOT.get()).key('X', Items.STICK).build(consumer, modLoc(name(TechworksItems.STEEL_PICKAXE.get())));
        shapedRecipe(result(TechworksItems.STEEL_AXE)).addCriterion("has_steel_ingot", hasItem(TechworksItems.STEEL_INGOT)).patternLine("##").patternLine("#X").patternLine(" X").key('#', TechworksItems.STEEL_INGOT.get()).key('X', Items.STICK).build(consumer, modLoc(name(TechworksItems.STEEL_AXE.get())));
        shapedRecipe(result(TechworksItems.STEEL_SHOVEL)).addCriterion("has_steel_ingot", hasItem(TechworksItems.STEEL_INGOT)).patternLine("#").patternLine("X").patternLine("X").key('#', TechworksItems.STEEL_INGOT.get()).key('X', Items.STICK).build(consumer, modLoc(name(TechworksItems.STEEL_SHOVEL.get())));
        shapedRecipe(result(TechworksItems.STEEL_HOE)).addCriterion("has_steel_ingot", hasItem(TechworksItems.STEEL_INGOT)).patternLine("##").patternLine(" X").patternLine(" X").key('#', TechworksItems.STEEL_INGOT.get()).key('X', Items.STICK).build(consumer, modLoc(name(TechworksItems.STEEL_HOE.get())));
        shapedRecipe(result(TechworksItems.STEEL_SWORD)).addCriterion("has_steel_ingot", hasItem(TechworksItems.STEEL_INGOT)).patternLine("#").patternLine("#").patternLine("X").key('#', TechworksItems.STEEL_INGOT.get()).key('X', Items.STICK).build(consumer, modLoc(name(TechworksItems.STEEL_SWORD.get())));
        shapedRecipe(result(TechworksItems.IRON_GEAR)).addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT)).patternLine(" # ").patternLine("#/#").patternLine(" # ").key('#', Tags.Items.INGOTS_IRON).key('/', Tags.Items.RODS_WOODEN).build(consumer, modLoc(name(TechworksItems.IRON_GEAR.get())));
        shapedRecipe(result(TechworksItems.ROCK_CRUSHER)).addCriterion("has_iron_gear", hasItem(TechworksItems.IRON_GEAR)).patternLine("PIP").patternLine("IGI").patternLine("WIW").key('P', TechworksItems.IRON_PLATE.get()).key('I', Items.IRON_INGOT).key('W', TechworksItems.COPPER_WIRE.get()).key('G', TechworksItems.IRON_GEAR.get()).build(consumer, modLoc(name(TechworksItems.ROCK_CRUSHER.get())));
        shapedRecipe(result(TechworksItems.ORE_CRUSHER)).addCriterion("has_rock_crusher", hasItem(TechworksItems.ROCK_CRUSHER)).patternLine("PSP").patternLine("SCS").patternLine("RSR").key('P', TechworksItems.IRON_DOUBLE_PLATE.get()).key('S', TechworksItems.STEEL_INGOT.get()).key('R', Blocks.REDSTONE_BLOCK).key('C', TechworksItems.ROCK_CRUSHER.get()).build(consumer, modLoc(name(TechworksItems.ORE_CRUSHER.get())));
        shapedRecipe(result(TechworksItems.STEAM_TURBINE_MK1)).addCriterion("has_iron_gear", hasItem(TechworksItems.IRON_GEAR)).patternLine("PWP").patternLine("GIG").patternLine("CWC").key('P', TechworksItems.IRON_PLATE.get()).key('G', TechworksItems.IRON_GEAR.get()).key('W', TechworksItems.COPPER_WIRE.get()).key('I', Items.IRON_INGOT).key('C', TechworksItems.COPPER_PLATE.get()).build(consumer, modLoc(name(TechworksItems.STEAM_TURBINE_MK1.get())));
        shapedRecipe(result(TechworksItems.ELECTRIC_DRILL)).addCriterion("has_steel_pickaxe", hasItem(TechworksItems.STEEL_PICKAXE)).patternLine("DRD").patternLine("GPG").patternLine("WSW").key('D', TechworksItems.IRON_DOUBLE_PLATE.get()).key('R', Blocks.REDSTONE_BLOCK).key('G', TechworksItems.IRON_GEAR.get()).key('P', TechworksItems.STEEL_PICKAXE.get()).key('W', TechworksItems.COPPER_WIRE.get()).key('S', TechworksItems.STEEL_INGOT.get()).build(consumer, modLoc(name(TechworksItems.ELECTRIC_DRILL.get())));
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

    private static void oreDouble(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> tag, Item dust) {
        GrinderRecipeBuilder.grinding(ingredient(tag), result(dust, 2), 1500).build(consumer, "grinder/" + name(dust) + "_from_ore");
    }

    private static void itemDouble(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> tag, ItemRegistryObject<Item> dust) {
        GrinderRecipeBuilder.grinding(ingredient(tag), result(dust, 2), 1500).build(consumer, "grinder/" + dust.getId().getPath());
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

    public static void plate(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> ingotTag, ItemRegistryObject<?> plate) {
        MetalPressingRecipeBuilder.plate(ingredient(ingotTag), result(plate)).build(consumer, "metal_press/" + plate.getId().getPath());
    }

    public static void gear(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> ingotTag, ItemRegistryObject<?> plate) {
        MetalPressingRecipeBuilder.gear(ingredient(ingotTag, 2), result(plate)).build(consumer, "metal_press/" + plate.getId().getPath());
    }

    private static void wire(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> ingotTag, ItemRegistryObject<Item> wire) {
        MetalPressingRecipeBuilder.wire(ingredient(ingotTag), result(wire, 2)).build(consumer, "metal_press/" + wire.getId().getPath());
    }

    private static void doublePlate(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> ingotTag, ItemRegistryObject<Item> plate) {
        MetalPressingRecipeBuilder.doublePlate(ingredient(ingotTag, 2), result(plate)).build(consumer, "metal_press/" + plate.getId().getPath());
    }

    public static void quadIngotFromOre(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> oreTag, Item ingot) {
        IndustrialSmeltingRecipeBuilder.smelting(Ingredient.fromTag(oreTag), result(ingot, 4), 450, 360000).build(consumer, "furnace/industrial/" + name(ingot));
    }

    public static void quadIngotFromOre(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> oreTag, ItemRegistryObject<?> ingot) {
        IndustrialSmeltingRecipeBuilder.smelting(Ingredient.fromTag(oreTag), result(ingot, 4), 450, 360000).build(consumer, "furnace/industrial/" + ingot.getId().getPath());
    }

    private static void alloy2Dusts1Ingot(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> dustTag, ITag.INamedTag<Item> ingotTag, ItemRegistryObject<Item> ingot) {
        IndustrialSmeltingRecipeBuilder.smelting(ingredient(dustTag, 2), ingredient(ingotTag), result(ingot), 450, 360000).build(consumer, "furnace/industrial/" + ingot.getId().getPath());
    }

    private static void hammeringPlate(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> ingotTag, ItemRegistryObject<?> plate) {
        HammeringRecipeBuilder.hammering(exactIngredient(ingotTag, 2), result(plate), 10).build(consumer, "hammering/" + plate.getId().getPath());
    }

    private static void wireCutting(Consumer<IFinishedRecipe> consumer, ITag.INamedTag<Item> tag, ItemRegistryObject<Item> wire) {
        WireCuttingRecipeBuilder.wireCutting(ingredient(tag, 2), result(wire)).build(consumer, "wire_cutting/" + wire.getId().getPath());
    }

    private static void hammeringDoublePlate(Consumer<IFinishedRecipe> consumer, Tags.IOptionalNamedTag<Item> ingotTag, ItemRegistryObject<Item> doublePlate) {
        HammeringRecipeBuilder.hammering(SizedIngredient.fromTag(ingotTag, 4, true), result(doublePlate), 15).build(consumer, "hammering/" + doublePlate.getId().getPath());
    }

    private static ShapedRecipeBuilder shapedRecipe(IRecipeResult result) {
        return ShapedRecipeBuilder.shapedRecipe(result.getItem(), result.getCount());
    }

    private static Ingredient ingredient(ITag.INamedTag<Item> tag) {
        return Ingredient.fromTag(tag);
    }

    private static Ingredient ingredient(IItemSupplier tag) {
        return Ingredient.fromItems(tag.getAsItem());
    }

    private static SizedIngredient ingredient(ITag.INamedTag<Item> tag, int count) {
        return SizedIngredient.fromTag(tag, count, false);
    }

    private static SizedIngredient exactIngredient(ITag.INamedTag<Item> tag, int count) {
        return SizedIngredient.fromTag(tag, count, true);
    }

    private static IRecipeResult result(IItemSupplier item) {
        return new RecipeResult(item, 1);
    }

    private static IRecipeResult result(IItemSupplier item, int count) {
        return new RecipeResult(item, count);
    }

    private static IRecipeResult result(IItemProvider item, int count) {
        return new RecipeResult(item, count);
    }

    private static ICriterionInstance hasItem(ITag.INamedTag<Item> tag) {
        ItemPredicate[] predicates = new ItemPredicate[] { ItemPredicate.Builder.create().tag(tag).build() };
        return new InventoryChangeTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, predicates);
    }

    private static ICriterionInstance hasItem(Item item) {
        ItemPredicate[] predicates = new ItemPredicate[] { ItemPredicate.Builder.create().item(item).build() };
        return new InventoryChangeTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, predicates);
    }

    private static ICriterionInstance hasItem(ItemRegistryObject<?> item) {
        ItemPredicate[] predicates = new ItemPredicate[] { ItemPredicate.Builder.create().item(item.get()).build() };
        return new InventoryChangeTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, predicates);
    }

    private static String name(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }

    private static ResourceLocation modLoc(String name) {
        return new ResourceLocation(Techworks.MOD_ID, name);
    }
}
