package dev.BloodyDreamsWork.backport;

import dev.BloodyDreamsWork.backport.mixin.BlockEntityTypeAccessor;
import dev.BloodyDreamsWork.backport.registry.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class ModEvents {

    public static void register() {
        extendBlockEntityTypes();
    }

    /**
     * The backported blocks reuse vanilla block entity types, which only list the vanilla blocks
     * they were registered with. Adding the poplar blocks keeps 26.3's mapping intact: signs,
     * hanging signs and shelves all share one type per kind, exactly as vanilla does.
     */
    private static void extendBlockEntityTypes() {
        allow(BlockEntityTypes.SIGN,
                ModBlocks.POPLAR_SIGN.get(),
                ModBlocks.POPLAR_WALL_SIGN.get());
        allow(BlockEntityTypes.HANGING_SIGN,
                ModBlocks.POPLAR_HANGING_SIGN.get(),
                ModBlocks.POPLAR_WALL_HANGING_SIGN.get());
        allow(BlockEntityTypes.SHELF, ModBlocks.POPLAR_SHELF.get());
    }

    private static void allow(BlockEntityType<?> type, Block... blocks) {
        BlockEntityTypeAccessor accessor = (BlockEntityTypeAccessor) type;
        Set<Block> extended = new HashSet<>(accessor.backport$getValidBlocks());
        extended.addAll(Arrays.asList(blocks));
        accessor.backport$setValidBlocks(extended);
    }

    private ModEvents() {
    }
}
