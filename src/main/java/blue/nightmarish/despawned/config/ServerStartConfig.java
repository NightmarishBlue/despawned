package blue.nightmarish.despawned.config;

import blue.nightmarish.despawned.Despawned;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import static blue.nightmarish.despawned.Despawned.LOGGER;

public class ServerStartConfig {
    public static void applyConfig() throws IllegalAccessException {
        int globalDespawnDistance = DespawnedCommonConfig.DEFAULT_DESPAWN_DISTANCE.get();
        int worldDespawnDistance = DespawnedWorldConfig.DESPAWN_DISTANCE.get();
        if (worldDespawnDistance == 0) worldDespawnDistance = globalDespawnDistance;
        ObfuscationReflectionHelper.findField(MobCategory.class, "despawnDistance").set(MobCategory.CREATURE, worldDespawnDistance);
        LOGGER.info("despawn distance is " + worldDespawnDistance);

        int globalNoDespawnDistance = DespawnedCommonConfig.DEFAULT_NO_DESPAWN_DISTANCE.get();
        int worldNoDespawnDistance = DespawnedWorldConfig.NO_DESPAWN_DISTANCE.get();
        if (worldNoDespawnDistance == 0) worldNoDespawnDistance = globalNoDespawnDistance;
        ObfuscationReflectionHelper.findField(MobCategory.class, "noDespawnDistance").set(MobCategory.CREATURE, worldNoDespawnDistance);
        LOGGER.info("no despawn distance is " + worldNoDespawnDistance);

        int globalMinAge = DespawnedCommonConfig.DEFAULT_MINIMUM_AGE.get();
        int worldMinAge = DespawnedWorldConfig.MINIMUM_AGE.get();
        if (worldMinAge == 0) worldMinAge = globalMinAge;
        Despawned.MINIMUM_AGE = worldMinAge;
        LOGGER.info("minimum despawn age (in ticks) is " + worldMinAge);
    }
}
