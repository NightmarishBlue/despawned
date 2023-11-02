package blue.nightmarish.despawned.mixin.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobCheckDespawn extends LivingEntity {
    //@Shadow public abstract boolean isPersistenceRequired();

    @Shadow private boolean persistenceRequired;

    protected MobCheckDespawn(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "isPersistenceRequired", at = @At("HEAD"), cancellable = true)
    void modifyPersistenceRequired(CallbackInfoReturnable<Boolean> cir) {
        // we're concerned with Animals.
        if ((Object) this instanceof Animal) {
            // a mob with the persistence tag shouldn't despawn
            if (this.persistenceRequired)
                { cir.setReturnValue(true); return; }
            // a tamed mob shouldn't despawn
            if ((Object) this instanceof TamableAnimal && ((TamableAnimal) (Object) this).isTame())
                { cir.setReturnValue(true); return; }
            // if we reach this point, just leave it be
        }
    }
}
