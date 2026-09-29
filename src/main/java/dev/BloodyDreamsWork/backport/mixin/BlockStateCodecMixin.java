package dev.BloodyDreamsWork.backport.mixin;

import com.mojang.serialization.Codec;
import dev.BloodyDreamsWork.backport.compat.CompactBlockStateCodec;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockState.class)
public class BlockStateCodecMixin {

    @Mutable
    @Shadow
    @Final
    public static Codec<BlockState> CODEC;

    /**
     * Wraps the codec at the end of its own static initializer, which is the one moment that is
     * guaranteed to be before anybody can have read the field: every other class that wants a block
     * state codec has to initialize this class first. That covers the whole save format at once -
     * the chunk palettes (PalettedContainerFactory.create feeds CODEC into the palette container
     * codec) as well as the entities, block entities and components that read their state with
     * input.read(..., BlockState.CODEC).
     *
     * <p>The CallbackInfo parameter is unused, but Mixin rejects a callback injector without it.
     */
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void backport$readNewerSaveFormat(CallbackInfo callback) {
        CODEC = new CompactBlockStateCodec(CODEC);
    }
}
