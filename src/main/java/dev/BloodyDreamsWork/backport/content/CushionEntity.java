package dev.BloodyDreamsWork.backport.content;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import dev.BloodyDreamsWork.backport.registry.ModItems;
import dev.BloodyDreamsWork.backport.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

import org.jspecify.annotations.Nullable;

public class CushionEntity extends Entity {

    public static final float HEIGHT = 0.25F;

    private static final EntityDataAccessor<Integer> DATA_COLOR =
            SynchedEntityData.defineId(CushionEntity.class, EntityDataSerializers.INT);

    private static final int SUPPORT_CHECK_INTERVAL = 20;

    private static final String COLOR_TAG = "color";

    /**
     * 26.3 saves the cushion colour by dye name ("light_blue", not the numeric id), which is what a
     * world has to contain for its cushions to keep their colour once it is opened in 26.3. The
     * numeric id is still read because earlier builds of this backport wrote it under the same key.
     */
    private static final Codec<DyeColor> COLOR_CODEC = Codec
            .either(DyeColor.CODEC, Codec.INT.xmap(DyeColor::byId, DyeColor::getId))
            .xmap(either -> either.map(Function.identity(), Function.identity()), Either::left);

    public CushionEntity(EntityType<? extends CushionEntity> type, Level level) {
        super(type, level);
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(DATA_COLOR));
    }

    public void setColor(DyeColor color) {
        this.entityData.set(DATA_COLOR, color.getId());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_COLOR, DyeColor.WHITE.getId());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.setColor(input.read(COLOR_TAG, COLOR_CODEC).orElse(DyeColor.WHITE));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.store(COLOR_TAG, COLOR_CODEC, this.getColor());
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()
                && this.tickCount % SUPPORT_CHECK_INTERVAL == 0
                && !this.hasSupport()) {
            this.breakCushion(null);
        }
    }

    private boolean hasSupport() {
        BlockPos below = BlockPos.containing(this.getX(), this.getY() - 0.05, this.getZ());
        VoxelShape shape = this.level().getBlockState(below).getCollisionShape(this.level(), below);
        if (shape.isEmpty()) {
            return false;
        }
        return below.getY() + shape.max(Direction.Axis.Y) >= this.getY() - 0.06;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty();
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (player.isSecondaryUseActive() || this.isVehicle()) {
            return InteractionResult.PASS;
        }

        if (!this.level().isClientSide() && player.startRiding(this)) {
            this.playSound(ModSounds.CUSHION_SIT.get(), 1.0F, 1.0F);
            this.gameEvent(GameEvent.ENTITY_INTERACT, player);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (!this.level().isClientSide()) {
            this.playSound(ModSounds.CUSHION_GET_UP.get(), 1.0F, 1.0F);
            this.gameEvent(GameEvent.ENTITY_INTERACT, passenger);
        }
    }

    @Override
    public boolean skipAttackInteraction(Entity attacker) {
        if (!(attacker instanceof Player)) {
            return false;
        }
        if (!this.level().isClientSide() && !this.isRemoved() && !this.isInvulnerable()) {
            this.breakCushion(attacker);
        }
        return true;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (this.isRemoved() || this.isInvulnerableToBase(source)) {
            return false;
        }
        this.breakCushion(source.getEntity());
        return true;
    }

    @Override
    public ItemStack getPickResult() {
        ItemStack stack = new ItemStack(ModItems.CUSHIONS.get(this.getColor()).get());
        if (this.hasCustomName()) {
            stack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
        }
        return stack;
    }

    private void breakCushion(@Nullable Entity source) {
        this.playSound(ModSounds.CUSHION_BREAK.get(), 1.0F, 1.0F);
        this.gameEvent(GameEvent.ENTITY_DIE, source);
        if (!(source instanceof Player player && player.getAbilities().instabuild)
                && this.level() instanceof ServerLevel serverLevel) {
            this.spawnAtLocation(serverLevel, this.getPickResult());
        }
        this.discard();
    }
}
