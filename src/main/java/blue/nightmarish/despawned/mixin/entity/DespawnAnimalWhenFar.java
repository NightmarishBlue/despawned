package blue.nightmarish.despawned.mixin.entity;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public abstract class DespawnAnimalWhenFar extends AgeableMob {
    protected DespawnAnimalWhenFar(EntityType<? extends AgeableMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "removeWhenFarAway", at = @At("HEAD"), cancellable = true)
    void shouldRemoveWhenFarAway(double pDistanceToClosestPlayer, CallbackInfoReturnable<Boolean> cir) {
        //if (this.random.nextInt(9) == 0) // a random chance of despawning to prevent animals from deleting after world gen
        if (true || this.noActionTime > 300) cir.setReturnValue(true); // a no action time of 300 (ticks?)
    }
}
