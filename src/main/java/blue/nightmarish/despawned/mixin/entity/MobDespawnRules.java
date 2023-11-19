package blue.nightmarish.despawned.mixin.entity;

import blue.nightmarish.despawned.DespawnRules;
import blue.nightmarish.despawned.Despawned;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static blue.nightmarish.despawned.Despawned.LOGGER;
import static blue.nightmarish.despawned.Despawned.VOLATILE_SPAWN_TYPES;

@Mixin(Mob.class)
public abstract class MobDespawnRules extends LivingEntity implements DespawnRules {
    @Unique
    private boolean hasDibs; // we can arbitrarily tag a creature by calling "dibs" on it

    protected MobDespawnRules(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Redirect(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isPersistenceRequired()Z"))
    boolean modifyPersistenceRequired(Mob instance) {
        // exit if this isn't an animal
        if (!(instance instanceof Animal)) return instance.isPersistenceRequired();

        // a mob with the persistence tag shouldn't despawn
        if (instance.isPersistenceRequired()) return true;

        // a mob whose spawn type exists and is not in the blacklist should not despawn
        // if the mob has no spawn type it probably shouldn't despawn (we have no idea how it got there)
        if (instance.getSpawnType() == null || !(VOLATILE_SPAWN_TYPES.contains(instance.getSpawnType()))) return true;

        // a tamed mob shouldn't despawn (why doesn't vanilla have an interface for this?)
        if (instance instanceof TamableAnimal && ((TamableAnimal) instance).isTame()) return true;
        if (instance instanceof AbstractHorse && ((AbstractHorse) instance).isTamed()) return true;

        // if this animal has been bred before, don't despawn
        if (((Animal) instance).getLoveCause() != null) return true;

        // a mob that can pick stuff up is probably special (and might have your stuff so despawning it is a bad idea)
        if (instance.canPickUpLoot()) return true;

        // if a mob is currently leashed, don't despawn it
        if (instance.isLeashed()) return true;

        // if this mob has "dibs" then don't despawn it either
        if (((DespawnRules) instance).hasDibs) return true;

        // no need to check if the mob is a passenger, that's handled
        return false;
    }

    @Inject(method = "setLeashedTo", at = @At("HEAD"))
    void callDibsWhenLeashed(Entity pLeashHolder, boolean pBroadcastPacket, CallbackInfo ci) {
        if ((Object) this instanceof Animal) {
            // if you've put a leash on a mob, it was probably to bring it somewhere
            this.hasDibs = true;
        }
    }

    @Redirect(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;discard()V"))
    public final void hookDiscard(Mob instance) {
        LOGGER.info(this.getDisplayName().getString() + " fucking died lel");
        instance.discard();
    }

    @Redirect(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;removeWhenFarAway(D)Z"))
    public boolean modifyRemoveWhenFarAway(Mob instance, double pDistanceToClosestPlayer) {
        // i guess we may have to put this logic in here if I can't figure this out.
        // there could be a better parameter to filter despawns than age but i can't think of a better one right now
        if (instance instanceof Animal && instance.tickCount > Despawned.MINIMUM_AGE) return true;
        return instance.removeWhenFarAway(pDistanceToClosestPlayer);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    public void addAdditionalAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
        if ((Object) this instanceof Animal) pCompound.putBoolean("HasDibs", this.hasDibs);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    public void readAdditionalAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
        if ((Object) this instanceof Animal) this.hasDibs = pCompound.getBoolean("HasDibs");
    }
}
