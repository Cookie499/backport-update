package dev.BloodyDreamsWork.backport.mixin;

import dev.BloodyDreamsWork.backport.BackportConfig;
import dev.BloodyDreamsWork.backport.content.FriendlyFire;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.ServerExplosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import org.jspecify.annotations.Nullable;

@Mixin(ServerExplosion.class)
public class ExplosionMixin {

    @Shadow
    @Final
    @Nullable
    private Entity source;

    @Redirect(
            method = "hurtEntities()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/ExplosionDamageCalculator;shouldDamageEntity"
                            + "(Lnet/minecraft/world/level/Explosion;Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean backport$spareTeammates(ExplosionDamageCalculator calculator,
                                            Explosion explosion, Entity target) {
        if (BackportConfig.vanillaBugfixes() && FriendlyFire.isProtected(this.source, target)) {
            return false;
        }
        return calculator.shouldDamageEntity(explosion, target);
    }
}
