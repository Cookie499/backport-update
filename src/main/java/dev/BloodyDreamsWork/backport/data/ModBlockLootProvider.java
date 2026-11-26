package dev.BloodyDreamsWork.backport.data;

import dev.BloodyDreamsWork.backport.content.ShelfMushroomBlock;
import dev.BloodyDreamsWork.backport.registry.ModBlocks;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Set;

public class ModBlockLootProvider extends BlockLootSubProvider {

    public ModBlockLootProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.POPLAR_LOG.get());
        dropSelf(ModBlocks.STRIPPED_POPLAR_LOG.get());
        dropSelf(ModBlocks.POPLAR_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_POPLAR_WOOD.get());
        dropSelf(ModBlocks.POPLAR_PLANKS.get());
        dropSelf(ModBlocks.POPLAR_STAIRS.get());
        dropSelf(ModBlocks.POPLAR_FENCE.get());
        dropSelf(ModBlocks.POPLAR_FENCE_GATE.get());
        dropSelf(ModBlocks.POPLAR_TRAPDOOR.get());
        dropSelf(ModBlocks.POPLAR_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.POPLAR_BUTTON.get());
        dropSelf(ModBlocks.POPLAR_SAPLING.get());
        dropSelf(ModBlocks.POPLAR_SIGN.get());
        dropSelf(ModBlocks.POPLAR_HANGING_SIGN.get());
        dropSelf(ModBlocks.POPLAR_SHELF.get());

        dropSelf(ModBlocks.RED_SHRUB.get());

        add(ModBlocks.SHELF_MUSHROOM.get(), block -> LootTable.lootTable()
                .withPool(applyExplosionCondition(block, LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(block)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                        .when(LootItemBlockStatePropertyCondition
                                                .hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(ShelfMushroomBlock.AGE, 1))))))));

        add(ModBlocks.STRAW_BED.get(),
                block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));

        add(ModBlocks.POPLAR_SLAB.get(), this::createSlabItemTable);
        add(ModBlocks.POPLAR_DOOR.get(), this::createDoorTable);

        add(ModBlocks.RED_POPLAR_LEAVES.get(),
                block -> createLeavesDrops(block, ModBlocks.POPLAR_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        add(ModBlocks.ORANGE_POPLAR_LEAVES.get(),
                block -> createLeavesDrops(block, ModBlocks.POPLAR_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        add(ModBlocks.YELLOW_POPLAR_LEAVES.get(),
                block -> createLeavesDrops(block, ModBlocks.POPLAR_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));

        for (DyeColor color : DyeColor.values()) {
            dropSelf(ModBlocks.WOOL_STAIRS.get(color).get());
            dropSelf(ModBlocks.CONCRETE_STAIRS.get(color).get());
            add(ModBlocks.WOOL_SLABS.get(color).get(), this::createSlabItemTable);
            add(ModBlocks.CONCRETE_SLABS.get(color).get(), this::createSlabItemTable);
        }

        // 26.2 keeps createPotFlowerItemTable private, so the pot drops are spelled out here.
        add(ModBlocks.POTTED_POPLAR_SAPLING.get(), block -> LootTable.lootTable()
                .withPool(applyExplosionCondition(Blocks.FLOWER_POT, LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Blocks.FLOWER_POT))))
                .withPool(applyExplosionCondition(ModBlocks.POPLAR_SAPLING.get(), LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModBlocks.POPLAR_SAPLING.get())))));
    }
}
