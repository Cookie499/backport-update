package dev.BloodyDreamsWork.backport.data;

import dev.BloodyDreamsWork.backport.content.ExplorerMapCloningRecipe;
import dev.BloodyDreamsWork.backport.registry.ModItems;
import dev.BloodyDreamsWork.backport.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        var planks = ModItems.POPLAR_PLANKS.get();
        Ingredient planksIngredient = Ingredient.of(planks);

        planksFromLogs(planks, ModTags.Items.POPLAR_LOGS, 4);
        woodFromLogs(ModItems.POPLAR_WOOD.get(), ModItems.POPLAR_LOG.get());
        woodFromLogs(ModItems.STRIPPED_POPLAR_WOOD.get(), ModItems.STRIPPED_POPLAR_LOG.get());

        stairBuilder(ModItems.POPLAR_STAIRS.get(), planksIngredient)
                .unlockedBy("has_planks", has(planks)).save(this.output);
        slab(RecipeCategory.BUILDING_BLOCKS, ModItems.POPLAR_SLAB.get(), planks);
        fenceBuilder(ModItems.POPLAR_FENCE.get(), planksIngredient)
                .unlockedBy("has_planks", has(planks)).save(this.output);
        fenceGateBuilder(ModItems.POPLAR_FENCE_GATE.get(), planksIngredient)
                .unlockedBy("has_planks", has(planks)).save(this.output);
        doorBuilder(ModItems.POPLAR_DOOR.get(), planksIngredient)
                .unlockedBy("has_planks", has(planks)).save(this.output);
        trapdoorBuilder(ModItems.POPLAR_TRAPDOOR.get(), planksIngredient)
                .unlockedBy("has_planks", has(planks)).save(this.output);
        pressurePlate(ModItems.POPLAR_PRESSURE_PLATE.get(), planks);
        buttonBuilder(ModItems.POPLAR_BUTTON.get(), planksIngredient)
                .unlockedBy("has_planks", has(planks)).save(this.output);

        signBuilder(ModItems.POPLAR_SIGN.get(), planksIngredient)
                .unlockedBy("has_planks", has(planks)).save(this.output);
        hangingSign(ModItems.POPLAR_HANGING_SIGN.get(), ModItems.STRIPPED_POPLAR_LOG.get());

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TRANSPORTATION, ModItems.POPLAR_BOAT.get())
                .define('P', planks)
                .pattern("P P")
                .pattern("PPP")
                .group("boat")
                .unlockedBy("in_water", insideOf(Blocks.WATER))
                .save(this.output);

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.TRANSPORTATION, ModItems.POPLAR_CHEST_BOAT.get())
                .requires(Blocks.CHEST)
                .requires(ModItems.POPLAR_BOAT.get())
                .group("chest_boat")
                .unlockedBy("has_boat", has(ItemTags.BOATS))
                .save(this.output);

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, Items.MUSHROOM_STEW)
                .requires(ModTags.Items.MUSHROOMS)
                .requires(ModTags.Items.MUSHROOMS)
                .requires(Items.BOWL)
                .unlockedBy("has_mushroom", has(ModTags.Items.MUSHROOMS))
                .save(this.output, "minecraft:mushroom_stew_from_any_mushrooms");

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, ModItems.STRAW_BED.get(), 4)
                .define('H', Blocks.HAY_BLOCK)
                .pattern("HHH")
                .unlockedBy("has_hay_block", has(Blocks.HAY_BLOCK))
                .save(this.output);

        SpecialRecipeBuilder.special(ExplorerMapCloningRecipe::new)
                .save(this.output, "minecraft:explorer_map_cloning");

        cushions();
        colouredStairsAndSlabs();
    }

    private void cushions() {
        for (DyeColor color : DyeColor.values()) {
            Item slab = ModItems.WOOL_SLABS.get(color).get();

            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, ModItems.CUSHIONS.get(color).get())
                    .define('S', slab)
                    .pattern("SSS")
                    .group("cushion")
                    .unlockedBy("has_wool_slab", has(slab))
                    .save(this.output);
        }
    }

    private void colouredStairsAndSlabs() {
        for (DyeColor color : DyeColor.values()) {
            source(color, "wool", ModItems.WOOL_STAIRS.get(color).get(),
                    ModItems.WOOL_SLABS.get(color).get());
            source(color, "concrete", ModItems.CONCRETE_STAIRS.get(color).get(),
                    ModItems.CONCRETE_SLABS.get(color).get());
        }
    }

    private void source(DyeColor color, String base, ItemLike stairs, ItemLike slab) {
        Item material = BuiltInRegistries.ITEM.getValue(
                Identifier.withDefaultNamespace(color.getName() + "_" + base));

        stairBuilder(stairs, Ingredient.of(material))
                .group(base + "_stairs")
                .unlockedBy("has_material", has(material))
                .save(this.output);
        slab(RecipeCategory.BUILDING_BLOCKS, slab, material);
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Backport Recipes";
        }
    }
}
