package dev.BloodyDreamsWork.backport.content;

import dev.BloodyDreamsWork.backport.worldgen.ModConfiguredFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public class PoplarSaplingBlock extends SaplingBlock {

    public static final TreeGrower GROWER = new TreeGrower(
            "minecraft:poplar",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.RED_POPLAR),
            Optional.empty());

    public PoplarSaplingBlock(BlockBehaviour.Properties properties) {
        super(GROWER, properties);
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 4);
            return;
        }

        ResourceKey<ConfiguredFeature<?, ?>> variant =
                ModConfiguredFeatures.POPLAR_VARIANTS[random.nextInt(ModConfiguredFeatures.POPLAR_VARIANTS.length)];

        ConfiguredFeature<?, ?> feature = level.registryAccess()
                .lookupOrThrow(Registries.CONFIGURED_FEATURE)
                .getValue(variant);
        if (feature == null) {
            return;
        }

        BlockState replacement = level.getFluidState(pos).createLegacyBlock();
        level.setBlock(pos, replacement, 4);
        if (feature.place(level, level.getChunkSource().getGenerator(), random, pos)) {
            if (level.getBlockState(pos) == replacement) {
                level.sendBlockUpdated(pos, state, replacement, 2);
            }
        } else {
            level.setBlock(pos, state, 4);
        }
    }
}
