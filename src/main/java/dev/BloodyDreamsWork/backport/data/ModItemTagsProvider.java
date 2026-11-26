package dev.BloodyDreamsWork.backport.data;

import dev.BloodyDreamsWork.backport.Backport;
import dev.BloodyDreamsWork.backport.registry.ModItems;
import dev.BloodyDreamsWork.backport.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output,
                               CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Backport.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.POPLAR_LOGS)
                .add(ModItems.POPLAR_LOG.get(),
                        ModItems.STRIPPED_POPLAR_LOG.get(),
                        ModItems.POPLAR_WOOD.get(),
                        ModItems.STRIPPED_POPLAR_WOOD.get());

        tag(ItemTags.LOGS_THAT_BURN).addTag(ModTags.Items.POPLAR_LOGS);
        tag(ItemTags.PLANKS).add(ModItems.POPLAR_PLANKS.get());
        tag(ItemTags.WOODEN_STAIRS).add(ModItems.POPLAR_STAIRS.get());
        tag(ItemTags.WOODEN_SLABS).add(ModItems.POPLAR_SLAB.get());
        tag(ItemTags.WOODEN_FENCES).add(ModItems.POPLAR_FENCE.get());
        tag(ItemTags.FENCE_GATES).add(ModItems.POPLAR_FENCE_GATE.get());
        tag(ItemTags.WOODEN_DOORS).add(ModItems.POPLAR_DOOR.get());
        tag(ItemTags.WOODEN_TRAPDOORS).add(ModItems.POPLAR_TRAPDOOR.get());
        tag(ItemTags.WOODEN_PRESSURE_PLATES).add(ModItems.POPLAR_PRESSURE_PLATE.get());
        tag(ItemTags.WOODEN_BUTTONS).add(ModItems.POPLAR_BUTTON.get());

        tag(ItemTags.LEAVES).add(
                ModItems.RED_POPLAR_LEAVES.get(),
                ModItems.ORANGE_POPLAR_LEAVES.get(),
                ModItems.YELLOW_POPLAR_LEAVES.get());

        tag(ItemTags.SAPLINGS).add(ModItems.POPLAR_SAPLING.get());
        tag(ItemTags.SIGNS).add(ModItems.POPLAR_SIGN.get());
        tag(ItemTags.HANGING_SIGNS).add(ModItems.POPLAR_HANGING_SIGN.get());
        tag(ItemTags.WOODEN_SHELVES).add(ModItems.POPLAR_SHELF.get());
        tag(ItemTags.BOATS).add(ModItems.POPLAR_BOAT.get());
        tag(ItemTags.CHEST_BOATS).add(ModItems.POPLAR_CHEST_BOAT.get());

        tag(ModTags.Items.MUSHROOMS)
                .add(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM)
                .add(ModItems.SHELF_MUSHROOM.get());

        var cloneable = tag(ModTags.Items.CLONEABLE_MAPS).add(Items.FILLED_MAP);
        ModItems.EXPLORER_MAPS.values().forEach(map -> cloneable.add(map.get()));
        tag(ModTags.Items.EXTENDABLE_MAPS).add(Items.FILLED_MAP);

        var woolStairs = tag(ModTags.Items.WOOL_STAIRS);
        var woolSlabs = tag(ModTags.Items.WOOL_SLABS);
        var concreteStairs = tag(ModTags.Items.CONCRETE_STAIRS);
        var concreteSlabs = tag(ModTags.Items.CONCRETE_SLABS);
        var allStairs = tag(ItemTags.STAIRS);
        var allSlabs = tag(ItemTags.SLABS);

        for (DyeColor color : DyeColor.values()) {
            var ws = ModItems.WOOL_STAIRS.get(color).get();
            var wl = ModItems.WOOL_SLABS.get(color).get();
            var cs = ModItems.CONCRETE_STAIRS.get(color).get();
            var cl = ModItems.CONCRETE_SLABS.get(color).get();

            woolStairs.add(ws);
            woolSlabs.add(wl);
            concreteStairs.add(cs);
            concreteSlabs.add(cl);
            allStairs.add(ws, cs);
            allSlabs.add(wl, cl);
        }
    }
}
