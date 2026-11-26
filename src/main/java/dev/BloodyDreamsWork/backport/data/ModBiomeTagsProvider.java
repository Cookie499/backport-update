package dev.BloodyDreamsWork.backport.data;

import dev.BloodyDreamsWork.backport.worldgen.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagsProvider extends BiomeTagsProvider {

    public ModBiomeTagsProvider(PackOutput output,
                                CompletableFuture<HolderLookup.Provider> lookupProvider) {
        // 26.2 takes the namespace from the registry, so no mod id argument any more.
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BiomeTags.IS_FOREST).add(ModBiomes.DAPPLED_FOREST);
        tag(BiomeTags.IS_OVERWORLD).add(ModBiomes.DAPPLED_FOREST);
    }
}
