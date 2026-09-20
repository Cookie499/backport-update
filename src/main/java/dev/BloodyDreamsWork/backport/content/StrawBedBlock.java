package dev.BloodyDreamsWork.backport.content;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.BloodyDreamsWork.backport.registry.ModSounds;
import dev.BloodyDreamsWork.backport.registry.ModStats;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jspecify.annotations.Nullable;

public class StrawBedBlock extends BedBlock {

    public static final MapCodec<BedBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(propertiesCodec()).apply(instance, StrawBedBlock::new));

    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);

    public StrawBedBlock(BlockBehaviour.Properties properties) {
        super(DyeColor.YELLOW, properties);
    }

    @Override
    public MapCodec<BedBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.CONSUME;
        }

        if (state.getValue(PART) != BedPart.HEAD) {
            pos = pos.relative(state.getValue(FACING));
            state = level.getBlockState(pos);
            if (!state.is(this)) {
                return InteractionResult.CONSUME;
            }
        }

        // Vanilla 26.3 gives straw beds their own STRAW_BED_RULE (DESTROY_ON_LEAVE) so that they can
        // be slept in even where a normal bed would blow up - they just break when you get up, which
        // this block already does when it crumbles. 26.2 has no STRAW_BED_RULE and no way for a mod
        // to add one (EnvironmentAttributes.register is private and minecraft-namespaced), so the
        // same behaviour is expressed here by simply not honouring the dimension's bed-explodes rule.
        if (state.getValue(OCCUPIED)) {
            player.sendOverlayMessage(Component.translatable("block.minecraft.bed.occupied"));
            return InteractionResult.SUCCESS;
        }

        player.startSleepInBed(pos)
                .ifLeft(problem -> {
                    if (problem.message() != null) {
                        player.sendOverlayMessage(problem.message());
                    }
                })
                .ifRight(success -> player.awardStat(ModStats.SLEEP_IN_STRAW_BED.get()));
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        crumble(level, pos, state);
    }

    private static void crumble(Level level, BlockPos pos, BlockState state) {
        BlockPos other = pos.relative(getConnectedDirection(state));
        level.removeBlock(pos, false);
        if (level.getBlockState(other).getBlock() instanceof StrawBedBlock) {
            level.removeBlock(other, false);
        }
        level.playSound(null, pos, ModSounds.STRAW_BED_BREAK_LEAVE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}
