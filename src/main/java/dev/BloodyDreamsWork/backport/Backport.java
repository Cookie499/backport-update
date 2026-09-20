package dev.BloodyDreamsWork.backport;

import dev.BloodyDreamsWork.backport.registry.ModBlocks;
import dev.BloodyDreamsWork.backport.registry.ModCreativeTabs;
import dev.BloodyDreamsWork.backport.registry.ModEntities;
import dev.BloodyDreamsWork.backport.registry.ModItems;
import dev.BloodyDreamsWork.backport.registry.ModLootFunctions;
import dev.BloodyDreamsWork.backport.registry.ModMapDecorations;
import dev.BloodyDreamsWork.backport.registry.ModParticles;
import dev.BloodyDreamsWork.backport.registry.ModRecipes;
import dev.BloodyDreamsWork.backport.registry.ModRegister;
import dev.BloodyDreamsWork.backport.registry.ModSounds;
import dev.BloodyDreamsWork.backport.registry.ModStats;
import dev.BloodyDreamsWork.backport.worldgen.ModWorldgenTypes;
import net.fabricmc.api.ModInitializer;

public class Backport implements ModInitializer {
    public static final String MODID = "minecraft";

    @Override
    public void onInitialize() {
        BackportConfig.load();

        ModSounds.register();
        ModBlocks.register();
        ModItems.register();
        ModEntities.register();
        ModMapDecorations.register();
        ModLootFunctions.register();
        ModParticles.register();
        ModRecipes.register();
        ModStats.register();
        ModWorldgenTypes.register();
        ModCreativeTabs.register();

        ModRegister.registerAll();

        ModBlocks.registerFlowerPots();
        ModBlocks.registerFlammability();

        ModEvents.register();
        ModGameEvents.register();
    }
}
