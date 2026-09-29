package dev.BloodyDreamsWork.backport.compat;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads the block state forms 26.3 writes in addition to the one this version writes, so a world
 * last played in 26.3 can be opened here instead of failing on its chunk palettes.
 *
 * <p>26.3 writes a block in its default state as just the block id ("minecraft:stone"), and the
 * other states under renamed keys: {id: "minecraft:poplar_log", properties: {axis: "x"}} instead of
 * {Name: ..., Properties: {...}}. The renamed object is translated back to the key names this
 * version's codec expects, so its own property parsing stays in charge.
 *
 * <p>Palettes that mix both shapes are padded with one-entry {"": state} compounds, because NBT
 * lists have to be homogeneous. ListTag peels those off again when a list is loaded, so a palette
 * normally reaches this codec as a mixture of plain strings and objects; the wrapper is still
 * accepted here for the reads that hand it over unpeeled (NBT streams, tags built in memory).
 *
 * <p>Structure templates are unaffected: 26.3 still writes those as {Name, Properties}
 * (MC-310816), and so does this version.
 *
 * <p>Everything still *writes* the form this version uses: the saved format is unchanged, and 26.3
 * upgrades it on the way in as it would any other world from this version.
 */
public final class CompactBlockStateCodec implements Codec<BlockState> {

    private static final String ID_TAG = "id";
    private static final String PROPERTIES_TAG = "properties";

    private final Codec<BlockState> legacy;
    private final Codec<Block> blockIds;

    public CompactBlockStateCodec(Codec<BlockState> legacy) {
        this.legacy = legacy;
        this.blockIds = BuiltInRegistries.BLOCK.byNameCodec();
    }

    @Override
    public <T> DataResult<Pair<BlockState, T>> decode(DynamicOps<T> ops, T input) {
        // 26.3 compact form: the block id on its own, meaning the block's default state.
        DataResult<Pair<Block, T>> compact = this.blockIds.decode(ops, input);
        if (compact.result().isPresent()) {
            return compact.map(pair -> pair.mapFirst(Block::defaultBlockState));
        }

        MapLike<T> asMap = ops.getMap(input).result().orElse(null);
        if (asMap != null) {
            // The {"": state} wrapper of a mixed-shape palette list.
            T wrapped = asMap.get("");
            if (wrapped != null) {
                return this.decode(ops, wrapped);
            }

            // 26.3's renamed object form.
            T id = asMap.get(ID_TAG);
            if (id != null) {
                return this.legacy.decode(ops, this.renameKeys(ops, asMap, id));
            }
        }

        return this.legacy.decode(ops, input);
    }

    /** Rebuilds {id, properties} under the key names this version's block state codec reads. */
    private <T> T renameKeys(DynamicOps<T> ops, MapLike<T> asMap, T id) {
        List<Pair<T, T>> fields = new ArrayList<>(2);
        fields.add(Pair.of(ops.createString(StateHolder.NAME_TAG), id));
        T properties = asMap.get(PROPERTIES_TAG);
        if (properties == null) {
            properties = asMap.get(StateHolder.PROPERTIES_TAG);
        }
        if (properties != null) {
            fields.add(Pair.of(ops.createString(StateHolder.PROPERTIES_TAG), properties));
        }
        return ops.createMap(fields.stream());
    }

    @Override
    public <T> DataResult<T> encode(BlockState input, DynamicOps<T> ops, T prefix) {
        return this.legacy.encode(input, ops, prefix);
    }

    @Override
    public String toString() {
        return "CompactBlockStateCodec[" + this.legacy + "]";
    }
}
