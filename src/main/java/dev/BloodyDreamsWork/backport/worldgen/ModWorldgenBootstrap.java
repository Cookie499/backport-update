package dev.BloodyDreamsWork.backport.worldgen;

import dev.BloodyDreamsWork.backport.registry.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.attribute.AmbientSounds;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FallenTreeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLogsDecorator;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;

import java.util.List;

public final class ModWorldgenBootstrap {

    private static final IntProvider FOLIAGE_RADIUS = new WeightedListInt(
            WeightedList.<IntProvider>builder()
                    .add(ConstantInt.of(5), 5)
                    .add(ConstantInt.of(6), 5)
                    .add(ConstantInt.of(7), 1)
                    .add(ConstantInt.of(8), 1)
                    .build());

    public static void configuredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        poplar(context, ModConfiguredFeatures.RED_POPLAR, ModBlocks.RED_POPLAR_LEAVES.get());
        poplar(context, ModConfiguredFeatures.ORANGE_POPLAR, ModBlocks.ORANGE_POPLAR_LEAVES.get());
        poplar(context, ModConfiguredFeatures.YELLOW_POPLAR, ModBlocks.YELLOW_POPLAR_LEAVES.get());

        // 26.2 already ships the fallen_tree feature and the attached_to_logs decorator, so the
        // backport uses the vanilla ones: registering our own under those ids is a duplicate key.
        context.register(ModConfiguredFeatures.FALLEN_POPLAR_TREE, new ConfiguredFeature<>(
                Feature.FALLEN_TREE,
                new FallenTreeConfiguration.FallenTreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.POPLAR_LOG.get()),
                        UniformInt.of(4, 7))
                        .logDecorators(List.of(
                                new AttachedToLogsDecorator(0.1F,
                                        BlockStateProvider.simple(Blocks.BROWN_MUSHROOM),
                                        List.of(Direction.UP)),
                                new ShelfMushroomDecorator(0.8F)))
                        .build()));

        context.register(ModConfiguredFeatures.RED_SHRUB, new ConfiguredFeature<>(
                Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.RED_SHRUB.get()))));

        treesDappledForest(context);
    }

    private static void poplar(BootstrapContext<ConfiguredFeature<?, ?>> context,
                               ResourceKey<ConfiguredFeature<?, ?>> key,
                               Block leaves) {
        context.register(key, new ConfiguredFeature<>(Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.POPLAR_LOG.get()),
                        new PoplarTrunkPlacer(7, 4, 0,
                                ConstantInt.of(4),
                                UniformInt.of(1, 4)),
                        BlockStateProvider.simple(leaves),
                        new PoplarFoliagePlacer(
                                FOLIAGE_RADIUS,
                                ConstantInt.of(0),
                                UniformInt.of(5, 6),
                                0.15F),
                        new TwoLayersFeatureSize(1, 0, 2),
                        BlockStateProvider.simple(Blocks.DIRT))
                        .decorators(List.of(new ShelfMushroomDecorator(0.4F)))
                        .ignoreVines()
                        .build()));
    }

    private static void treesDappledForest(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);

        context.register(ModConfiguredFeatures.TREES_DAPPLED_FOREST, new ConfiguredFeature<>(
                Feature.RANDOM_SELECTOR,
                new RandomFeatureConfiguration(
                        List.of(
                                new WeightedPlacedFeature(
                                        placed.getOrThrow(ModPlacedFeatures.RED_POPLAR_CHECKED), 0.2954F),
                                new WeightedPlacedFeature(
                                        placed.getOrThrow(ModPlacedFeatures.ORANGE_POPLAR_CHECKED), 0.5031F),
                                new WeightedPlacedFeature(
                                        placed.getOrThrow(ModPlacedFeatures.YELLOW_POPLAR_CHECKED), 0.3797F),
                                new WeightedPlacedFeature(
                                        placed.getOrThrow(TreePlacements.SPRUCE_CHECKED), 0.1837F)),
                        placed.getOrThrow(ModPlacedFeatures.FALLEN_POPLAR_TREE_CHECKED))));
    }

    public static void placedFeatures(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        Block sapling = ModBlocks.POPLAR_SAPLING.get();

        checkedTree(context, ModPlacedFeatures.RED_POPLAR_CHECKED,
                features, ModConfiguredFeatures.RED_POPLAR, sapling);
        checkedTree(context, ModPlacedFeatures.ORANGE_POPLAR_CHECKED,
                features, ModConfiguredFeatures.ORANGE_POPLAR, sapling);
        checkedTree(context, ModPlacedFeatures.YELLOW_POPLAR_CHECKED,
                features, ModConfiguredFeatures.YELLOW_POPLAR, sapling);
        checkedTree(context, ModPlacedFeatures.FALLEN_POPLAR_TREE_CHECKED,
                features, ModConfiguredFeatures.FALLEN_POPLAR_TREE, sapling);

        context.register(ModPlacedFeatures.TREES_DAPPLED_FOREST, new PlacedFeature(
                features.getOrThrow(ModConfiguredFeatures.TREES_DAPPLED_FOREST),
                List.of(CountPlacement.of(6),
                        InSquarePlacement.spread(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        BiomeFilter.biome())));

        context.register(ModPlacedFeatures.BAMBOO_IN_STRUCTURE, new PlacedFeature(
                features.getOrThrow(VegetationFeatures.BAMBOO_NO_PODZOL),
                List.of(BlockPredicateFilter.forPredicate(BlockPredicate.matchesTag(BlockTags.AIR)))));

        context.register(ModPlacedFeatures.PATCH_RED_SHRUB, new PlacedFeature(
                features.getOrThrow(ModConfiguredFeatures.RED_SHRUB),
                List.of(InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome())));

        context.register(ModPlacedFeatures.BROWN_MUSHROOM_DAPPLED_FOREST, new PlacedFeature(
                features.getOrThrow(VegetationFeatures.BROWN_MUSHROOM),
                List.of(RarityFilter.onAverageOnceEvery(2),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome())));
    }

    private static void checkedTree(BootstrapContext<PlacedFeature> context,
                                    ResourceKey<PlacedFeature> key,
                                    HolderGetter<ConfiguredFeature<?, ?>> features,
                                    ResourceKey<ConfiguredFeature<?, ?>> feature,
                                    Block sapling) {
        context.register(key, new PlacedFeature(features.getOrThrow(feature),
                List.of(PlacementUtils.filteredByBlockSurvival(sapling))));
    }

    public static void biomes(BootstrapContext<Biome> context) {
        context.register(ModBiomes.DAPPLED_FOREST, dappledForest(
                context.lookup(Registries.PLACED_FEATURE),
                context.lookup(Registries.CONFIGURED_CARVER)));
    }

    private static Biome dappledForest(HolderGetter<PlacedFeature> placed,
                                       HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(spawns);
        BiomeDefaultFeatures.farmAnimals(spawns);
        spawns.addSpawn(MobCategory.CREATURE, 4, new MobSpawnSettings.SpawnerData(EntityTypes.RABBIT, 2, 4));
        spawns.addSpawn(MobCategory.CREATURE, 4, new MobSpawnSettings.SpawnerData(EntityTypes.FOX, 2, 4));

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placed, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultCrystalFormations(generation);
        BiomeDefaultFeatures.addDefaultMonsterRoom(generation);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generation);
        BiomeDefaultFeatures.addDefaultSprings(generation);
        BiomeDefaultFeatures.addSurfaceFreezing(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.TREES_DAPPLED_FOREST);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.BROWN_MUSHROOM_DAPPLED_FOREST);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PATCH_RED_SHRUB);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST);

        BiomeSpecialEffects effects = new BiomeSpecialEffects.Builder()
                .waterColor(0x375154)
                .grassColorOverride(0xDF6827)
                .foliageColorOverride(0xE68E30)
                .build();

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.6F)
                .downfall(0.6F)
                .specialEffects(effects)
                .setAttribute(EnvironmentAttributes.FOG_COLOR, 0xCCD8E2)
                .setAttribute(EnvironmentAttributes.SKY_COLOR, 0x7CA3FF)
                .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, 0x375154)
                .setAttribute(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS)
                .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_FLOWER_FOREST))
                .mobSpawnSettings(spawns.build())
                .generationSettings(generation.build())
                .build();
    }

    private ModWorldgenBootstrap() {
    }
}
