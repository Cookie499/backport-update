package dev.BloodyDreamsWork.backport.mixin;

import com.mojang.datafixers.DSL.TypeReference;
import com.mojang.datafixers.types.Type;
import net.minecraft.SharedConstants;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Util.class)
public class DataFixerSchemaMixin {

    /**
     * The entity types this backport adds that the running version's data fixer schema has never
     * heard of. Keep in step with ModEntities.
     */
    private static final Set<String> MODDED_ENTITY_IDS = Set.of(
            "minecraft:cushion",
            "minecraft:poplar_boat",
            "minecraft:poplar_chest_boat");

    /**
     * EntityType.Builder#build asks the data fixer schema for a type by id and Minecraft treats a
     * miss as an error. A backport necessarily introduces entity types the running version has never
     * heard of, so a miss for those is expected rather than a mistake. Outside the IDE it logs
     * "No data fixer registered for <id>"; because Util rethrows when
     * SharedConstants.IS_RUNNING_IN_IDE is set, it crashes a development client outright.
     *
     * This answers the lookup with the same null the vanilla failure path would have produced, but
     * without going through the failing lookup. The check cannot use the entity registry: build()
     * runs before the type is registered, so at this point it is not in the registry yet.
     */
    @Inject(method = "doFetchChoiceType", at = @At("HEAD"), cancellable = true)
    private static void backport$skipUnknownEntityDataFixer(TypeReference reference, String name,
                                                            CallbackInfoReturnable<Type<?>> callback) {
        if (!SharedConstants.CHECK_DATA_FIXER_SCHEMA) {
            return;
        }
        if (MODDED_ENTITY_IDS.contains(name)) {
            callback.setReturnValue(null);
        }
    }
}
