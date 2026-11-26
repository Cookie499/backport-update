package dev.BloodyDreamsWork.backport.data;

import dev.BloodyDreamsWork.backport.content.ExplorerMapCloningRecipe;
import dev.BloodyDreamsWork.backport.registry.ModItems;
import dev.BloodyDreamsWork.backport.registry.ModTags;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.EnterBlockTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;
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

        // 26.2 keeps the fence, fence gate and sign shapes private to RecipeProvider, so they are
        // spelled out here the same way vanilla builds them.
        shaped(RecipeCategory.DECORATIONS, ModItems.POPLAR_FENCE.get(), 3)
                .define('W', planksIngredient)
                .define('#', Items.STICK)
                .pattern("W#W")
                .pattern("W#W")
                .unlockedBy("has_planks", has(planks))
                .save(this.output);
        shaped(RecipeCategory.REDSTONE, ModItems.POPLAR_FENCE_GATE.get())
                .define('#', Items.STICK)
                .define('W', planksIngredient)
                .pattern("#W#")
                .pattern("#W#")
                .unlockedBy("has_planks", has(planks))
                .save(this.output);
        doorBuilder(ModItems.POPLAR_DOOR.get(), planksIngredient)
                .unlockedBy("has_planks", has(planks)).save(this.output);
        trapdoorBuilder(ModItems.POPLAR_TRAPDOOR.get(), planksIngredient)
                .unlockedBy("has_planks", has(planks)).save(this.output);
        pressurePlate(ModItems.POPLAR_PRESSURE_PLATE.get(), planks);
        shapeless(RecipeCategory.REDSTONE, ModItems.POPLAR_BUTTON.get())
                .requires(planksIngredient)
                .unlockedBy("has_planks", has(planks))
                .save(this.output);

        shaped(RecipeCategory.DECORATIONS, ModItems.POPLAR_SIGN.get(), 3)
                .group("sign")
                .define('#', planksIngredient)
                .define('X', Items.STICK)
                .pattern("###")
                .pattern("###")
                .pattern(" X ")
                .unlockedBy("has_planks", has(planks))
                .save(this.output);
        hangingSignBuilder(ModItems.POPLAR_HANGING_SIGN.get(), Ingredient.of(ModItems.STRIPPED_POPLAR_LOG.get()))
                .unlockedBy("has_stripped_logs", has(ModItems.STRIPPED_POPLAR_LOG.get()))
                .save(this.output);

        shelf(ModItems.POPLAR_SHELF.get(), ModItems.STRIPPED_POPLAR_LOG.get());

        shaped(RecipeCategory.TRANSPORTATION, ModItems.POPLAR_BOAT.get())
                .define('P', planks)
                .pattern("P P")
                .pattern("PPP")
                .group("boat")
                .unlockedBy("in_water", insideOf(Blocks.WATER))
                .save(this.output);

        shapeless(RecipeCategory.TRANSPORTATION, ModItems.POPLAR_CHEST_BOAT.get())
                .requires(Blocks.CHEST)
                .requires(ModItems.POPLAR_BOAT.get())
                .group("chest_boat")
                .unlockedBy("has_boat", has(ItemTags.BOATS))
                .save(this.output);

        shapeless(RecipeCategory.FOOD, Items.MUSHROOM_STEW)
                .requires(ModTags.Items.MUSHROOMS)
                .requires(ModTags.Items.MUSHROOMS)
                .requires(Items.BOWL)
                .unlockedBy("has_mushroom", has(ModTags.Items.MUSHROOMS))
                .save(this.output, "minecraft:mushroom_stew_from_any_mushrooms");

        shaped(RecipeCategory.DECORATIONS, ModItems.STRAW_BED.get(), 4)
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

            shaped(RecipeCategory.DECORATIONS, ModItems.CUSHIONS.get(color).get())
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

    /** Vanilla keeps the "enter water" criterion private; this is the same criterion it builds. */
    private static Criterion<EnterBlockTrigger.TriggerInstance> insideOf(Block block) {
        return CriteriaTriggers.ENTER_BLOCK.createCriterion(new EnterBlockTrigger.TriggerInstance(
                Optional.empty(), Optional.of(block.builtInRegistryHolder()), Optional.empty()));
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
