package blue.nightmarish.despawned.mixin.entity;

import blue.nightmarish.despawned.Despawned;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

import static blue.nightmarish.despawned.Despawned.LOGGER;
import static blue.nightmarish.despawned.Despawned.VOLATILE_SPAWN_TYPES;

@Mixin(Mob.class)
public abstract class MobCheckDespawn extends LivingEntity {
    //@Shadow public abstract boolean isPersistenceRequired();
    @Nullable @Shadow
    private MobSpawnType spawnType;

    @Shadow private boolean persistenceRequired;

    protected MobCheckDespawn(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "isPersistenceRequired", at = @At("HEAD"), cancellable = true)
    void modifyPersistenceRequired(CallbackInfoReturnable<Boolean> cir) {
        // we're concerned with Animals.
        if ((Object) this instanceof Animal) {
//            Despawned.LOGGER.info("unless???");
            // a mob with the persistence tag shouldn't despawn
            if (this.persistenceRequired) { cir.setReturnValue(true); return; }
            // a mob whose spawn type exists and is not in the blacklist should not despawn
            if (this.spawnType != null && !(VOLATILE_SPAWN_TYPES.contains(this.spawnType))) { cir.setReturnValue(true); return; }
            // a tamed mob shouldn't despawn
            if ((Object) this instanceof TamableAnimal && ((TamableAnimal) (Object) this).isTame())
                { cir.setReturnValue(true); return; }
            // if we reach this point, just leave it be
//            LOGGER.info(this.getDisplayName().getString() + " is trying to despawn and its persistence is " + String.valueOf(cir.getReturnValue()));
        }
    }

    @Inject(method = "mobInteract", at = @At("HEAD"))
    void onInteract(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> cir) {
        String spawntype = "NULL";
        if (this.spawnType != null) spawntype = this.spawnType.name();
        LOGGER.info("this mob is a " + this.getName().getString());
        LOGGER.info("its spawn type is " + spawntype);
        if (this.spawnType != null && VOLATILE_SPAWN_TYPES.contains(this.spawnType)) Despawned.LOGGER.info("this is in the volatile spawn category");
        LOGGER.info("the despawn distance is " + this.getType().getCategory().getDespawnDistance());
    }

    @Redirect(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;discard()V"))
    public final void hookDiscard(Mob instance) {
        LOGGER.info(this.getDisplayName().getString() + " fucking died lel");
        instance.discard();
    }

    @Redirect(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;removeWhenFarAway(D)Z"))
    public boolean modifyRemoveWhenFarAway(Mob instance, double pDistanceToClosestPlayer) {
        // i guess we may have to put this logic in here if I can't figure this out.
        if (instance instanceof Animal && instance.tickCount > 6000) return true;
        return instance.removeWhenFarAway(pDistanceToClosestPlayer);
    }
}
