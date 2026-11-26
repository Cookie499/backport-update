package dev.BloodyDreamsWork.backport.data;

import dev.BloodyDreamsWork.backport.Backport;
import dev.BloodyDreamsWork.backport.registry.ModBlocks;
import dev.BloodyDreamsWork.backport.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output,
                                CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Backport.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Blocks.POPLAR_LOGS)
                .add(ModBlocks.POPLAR_LOG.get(),
                        ModBlocks.STRIPPED_POPLAR_LOG.get(),
                        ModBlocks.POPLAR_WOOD.get(),
                        ModBlocks.STRIPPED_POPLAR_WOOD.get());

        tag(BlockTags.LOGS_THAT_BURN).addTag(ModTags.Blocks.POPLAR_LOGS);
        tag(BlockTags.PLANKS).add(ModBlocks.POPLAR_PLANKS.get());
        tag(BlockTags.WOODEN_STAIRS).add(ModBlocks.POPLAR_STAIRS.get());
        tag(BlockTags.WOODEN_SLABS).add(ModBlocks.POPLAR_SLAB.get());
        tag(BlockTags.WOODEN_FENCES).add(ModBlocks.POPLAR_FENCE.get());
        tag(BlockTags.FENCE_GATES).add(ModBlocks.POPLAR_FENCE_GATE.get());
        tag(BlockTags.WOODEN_DOORS).add(ModBlocks.POPLAR_DOOR.get());
        tag(BlockTags.WOODEN_TRAPDOORS).add(ModBlocks.POPLAR_TRAPDOOR.get());
        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.POPLAR_PRESSURE_PLATE.get());
        tag(BlockTags.WOODEN_BUTTONS).add(ModBlocks.POPLAR_BUTTON.get());

        tag(BlockTags.LEAVES).add(
                ModBlocks.RED_POPLAR_LEAVES.get(),
                ModBlocks.ORANGE_POPLAR_LEAVES.get(),
                ModBlocks.YELLOW_POPLAR_LEAVES.get());

        tag(BlockTags.SAPLINGS).add(ModBlocks.POPLAR_SAPLING.get());
        tag(BlockTags.FLOWER_POTS).add(ModBlocks.POTTED_POPLAR_SAPLING.get());

        tag(BlockTags.STANDING_SIGNS).add(ModBlocks.POPLAR_SIGN.get());
        tag(BlockTags.WALL_SIGNS).add(ModBlocks.POPLAR_WALL_SIGN.get());
        tag(BlockTags.CEILING_HANGING_SIGNS).add(ModBlocks.POPLAR_HANGING_SIGN.get());
        tag(BlockTags.WALL_HANGING_SIGNS).add(ModBlocks.POPLAR_WALL_HANGING_SIGN.get());
        tag(BlockTags.WOODEN_SHELVES).add(ModBlocks.POPLAR_SHELF.get());

        tag(BlockTags.MINEABLE_WITH_AXE).add(
                ModBlocks.POPLAR_LOG.get(),
                ModBlocks.STRIPPED_POPLAR_LOG.get(),
                ModBlocks.POPLAR_WOOD.get(),
                ModBlocks.STRIPPED_POPLAR_WOOD.get(),
                ModBlocks.POPLAR_PLANKS.get(),
                ModBlocks.POPLAR_STAIRS.get(),
                ModBlocks.POPLAR_SLAB.get(),
                ModBlocks.POPLAR_FENCE.get(),
                ModBlocks.POPLAR_FENCE_GATE.get(),
                ModBlocks.POPLAR_DOOR.get(),
                ModBlocks.POPLAR_TRAPDOOR.get(),
                ModBlocks.POPLAR_PRESSURE_PLATE.get(),
                ModBlocks.POPLAR_BUTTON.get(),
                ModBlocks.POPLAR_SIGN.get(),
                ModBlocks.POPLAR_WALL_SIGN.get(),
                ModBlocks.POPLAR_HANGING_SIGN.get(),
                ModBlocks.POPLAR_WALL_HANGING_SIGN.get());

        tag(BlockTags.MINEABLE_WITH_HOE).add(
                ModBlocks.RED_POPLAR_LEAVES.get(),
                ModBlocks.ORANGE_POPLAR_LEAVES.get(),
                ModBlocks.YELLOW_POPLAR_LEAVES.get(),
                ModBlocks.SHELF_MUSHROOM.get());

        tag(BlockTags.SWORD_EFFICIENT).add(ModBlocks.RED_SHRUB.get());
        tag(BlockTags.REPLACEABLE_BY_TREES).add(ModBlocks.RED_SHRUB.get());

        tag(ModTags.Blocks.CUSHION_USES_COLLISION_SHAPE)
                .addTag(BlockTags.CAULDRONS)
                .add(Blocks.HOPPER, Blocks.COMPOSTER);

        colouredStairsAndSlabs();

        tag(BlockTags.COMPLETES_FIND_TREE_TUTORIAL)
                .addTag(ModTags.Blocks.POPLAR_LOGS)
                .add(ModBlocks.RED_POPLAR_LEAVES.get(),
                        ModBlocks.ORANGE_POPLAR_LEAVES.get(),
                        ModBlocks.YELLOW_POPLAR_LEAVES.get());
    }

    private void colouredStairsAndSlabs() {
        var woolStairs = tag(ModTags.Blocks.WOOL_STAIRS);
        var woolSlabs = tag(ModTags.Blocks.WOOL_SLABS);
        var concreteStairs = tag(ModTags.Blocks.CONCRETE_STAIRS);
        var concreteSlabs = tag(ModTags.Blocks.CONCRETE_SLABS);
        var allStairs = tag(BlockTags.STAIRS);
        var allSlabs = tag(BlockTags.SLABS);
        var pickaxe = tag(BlockTags.MINEABLE_WITH_PICKAXE);
        var wool = tag(BlockTags.WOOL);

        for (DyeColor color : DyeColor.values()) {
            Block ws = ModBlocks.WOOL_STAIRS.get(color).get();
            Block wl = ModBlocks.WOOL_SLABS.get(color).get();
            Block cs = ModBlocks.CONCRETE_STAIRS.get(color).get();
            Block cl = ModBlocks.CONCRETE_SLABS.get(color).get();

            woolStairs.add(ws);
            woolSlabs.add(wl);
            concreteStairs.add(cs);
            concreteSlabs.add(cl);

            allStairs.add(ws, cs);
            allSlabs.add(wl, cl);

            pickaxe.add(cs, cl);

            wool.add(ws, wl);
        }
    }
}
