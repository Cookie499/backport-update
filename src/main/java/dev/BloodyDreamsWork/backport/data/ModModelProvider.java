package dev.BloodyDreamsWork.backport.data;

import com.mojang.math.Quadrant;
import dev.BloodyDreamsWork.backport.Backport;
import dev.BloodyDreamsWork.backport.content.ExplorerMapType;
import dev.BloodyDreamsWork.backport.content.ShelfMushroomBlock;
import dev.BloodyDreamsWork.backport.registry.ModBlocks;
import dev.BloodyDreamsWork.backport.registry.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BedPart;

public class ModModelProvider extends ModelProvider {

    public ModModelProvider(PackOutput output) {
        super(output, Backport.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.woodProvider(ModBlocks.POPLAR_LOG.get())
                .logWithHorizontal(ModBlocks.POPLAR_LOG.get())
                .wood(ModBlocks.POPLAR_WOOD.get());
        blockModels.woodProvider(ModBlocks.STRIPPED_POPLAR_LOG.get())
                .logWithHorizontal(ModBlocks.STRIPPED_POPLAR_LOG.get())
                .wood(ModBlocks.STRIPPED_POPLAR_WOOD.get());
        blockModels.createHangingSign(ModBlocks.STRIPPED_POPLAR_LOG.get(),
                ModBlocks.POPLAR_HANGING_SIGN.get(), ModBlocks.POPLAR_WALL_HANGING_SIGN.get());

        BlockFamily poplarFamily = new BlockFamily.Builder(ModBlocks.POPLAR_PLANKS.get())
                .stairs(ModBlocks.POPLAR_STAIRS.get())
                .slab(ModBlocks.POPLAR_SLAB.get())
                .fence(ModBlocks.POPLAR_FENCE.get())
                .fenceGate(ModBlocks.POPLAR_FENCE_GATE.get())
                .pressurePlate(ModBlocks.POPLAR_PRESSURE_PLATE.get())
                .button(ModBlocks.POPLAR_BUTTON.get())
                .door(ModBlocks.POPLAR_DOOR.get())
                .trapdoor(ModBlocks.POPLAR_TRAPDOOR.get())
                .sign(ModBlocks.POPLAR_SIGN.get(), ModBlocks.POPLAR_WALL_SIGN.get())
                .dontGenerateRecipe()
                .getFamily();
        blockModels.family(ModBlocks.POPLAR_PLANKS.get()).generateFor(poplarFamily);

        blockModels.createTrivialBlock(ModBlocks.RED_POPLAR_LEAVES.get(), TexturedModel.LEAVES);
        blockModels.createTrivialBlock(ModBlocks.ORANGE_POPLAR_LEAVES.get(), TexturedModel.LEAVES);
        blockModels.createTrivialBlock(ModBlocks.YELLOW_POPLAR_LEAVES.get(), TexturedModel.LEAVES);

        blockModels.createPlantWithDefaultItem(ModBlocks.POPLAR_SAPLING.get(),
                ModBlocks.POTTED_POPLAR_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        shelfMushroom(blockModels);
        blockModels.createCrossBlockWithDefaultItem(ModBlocks.RED_SHRUB.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        strawBed(blockModels);

        for (DyeColor color : DyeColor.values()) {
            colouredStairsAndSlabs(blockModels, color, "wool",
                    ModBlocks.WOOL_STAIRS.get(color).get(), ModBlocks.WOOL_SLABS.get(color).get());
            colouredStairsAndSlabs(blockModels, color, "concrete",
                    ModBlocks.CONCRETE_STAIRS.get(color).get(), ModBlocks.CONCRETE_SLABS.get(color).get());
        }

        itemModels.generateFlatItem(ModItems.POPLAR_BOAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.POPLAR_CHEST_BOAT.get(), ModelTemplates.FLAT_ITEM);

        for (DyeColor color : DyeColor.values()) {
            itemModels.generateFlatItem(ModItems.CUSHIONS.get(color).get(), ModelTemplates.FLAT_ITEM);
        }

        for (ExplorerMapType type : ExplorerMapType.values()) {
            flatItem(itemModels, ModItems.EXPLORER_MAPS.get(type).get(), "item/" + type.textureName());
        }

        existingModelItem(itemModels, ModItems.SHELF_MUSHROOM.get(), "block/shelf_mushroom_stage0");
    }

    private void shelfMushroom(BlockModelGenerators blockModels) {
        Identifier small = Identifier.fromNamespaceAndPath(Backport.MODID, "block/shelf_mushroom_stage0");
        Identifier large = Identifier.fromNamespaceAndPath(Backport.MODID, "block/shelf_mushroom_stage1");

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(ModBlocks.SHELF_MUSHROOM.get())
                        .with(PropertyDispatch.initial(ShelfMushroomBlock.AGE)
                                .select(0, BlockModelGenerators.plainVariant(small))
                                .select(1, BlockModelGenerators.plainVariant(large)))
                        .with(PropertyDispatch.modify(ShelfMushroomBlock.FACING)
                                .select(Direction.NORTH, variant -> variant)
                                .select(Direction.EAST, VariantMutator.Y_ROT.withValue(Quadrant.R90))
                                .select(Direction.SOUTH, VariantMutator.Y_ROT.withValue(Quadrant.R180))
                                .select(Direction.WEST, VariantMutator.Y_ROT.withValue(Quadrant.R270))));
    }

    private void strawBed(BlockModelGenerators blockModels) {
        Identifier foot = Identifier.fromNamespaceAndPath(Backport.MODID, "block/straw_bed_foot");
        Identifier head = Identifier.fromNamespaceAndPath(Backport.MODID, "block/straw_bed_head");

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(ModBlocks.STRAW_BED.get())
                        .with(PropertyDispatch.initial(BedBlock.PART)
                                .select(BedPart.HEAD, BlockModelGenerators.plainVariant(head))
                                .select(BedPart.FOOT, BlockModelGenerators.plainVariant(foot)))
                        .with(PropertyDispatch.modify(BedBlock.FACING)
                                .select(Direction.SOUTH, variant -> variant)
                                .select(Direction.WEST, VariantMutator.Y_ROT.withValue(Quadrant.R90))
                                .select(Direction.NORTH, VariantMutator.Y_ROT.withValue(Quadrant.R180))
                                .select(Direction.EAST, VariantMutator.Y_ROT.withValue(Quadrant.R270))));
    }

    private void colouredStairsAndSlabs(BlockModelGenerators blockModels, DyeColor color, String base,
                                        StairBlock stairs, SlabBlock slab) {
        Block material = vanilla(color, base);
        BlockFamily family = new BlockFamily.Builder(material)
                .stairs(stairs)
                .slab(slab)
                .dontGenerateRecipe()
                .getFamily();
        blockModels.familyWithExistingFullBlock(material).generateFor(family);
    }

    private static Block vanilla(DyeColor color, String suffix) {
        return BuiltInRegistries.BLOCK.getValue(
                Identifier.withDefaultNamespace(color.getName() + "_" + suffix));
    }

    private void flatItem(ItemModelGenerators itemModels, Item item, String texturePath) {
        Identifier modelId = ModelTemplates.FLAT_ITEM.create(
                ModelLocationUtils.getModelLocation(item),
                TextureMapping.layer0(Identifier.fromNamespaceAndPath(Backport.MODID, texturePath)),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item,
                net.minecraft.client.data.models.model.ItemModelUtils.plainModel(modelId));
    }

    private void existingModelItem(ItemModelGenerators itemModels, Item item, String modelPath) {
        itemModels.itemModelOutput.accept(item,
                net.minecraft.client.data.models.model.ItemModelUtils.plainModel(
                        Identifier.fromNamespaceAndPath(Backport.MODID, modelPath)));
    }
}
