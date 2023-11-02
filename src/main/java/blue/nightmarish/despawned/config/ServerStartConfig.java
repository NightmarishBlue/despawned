package blue.nightmarish.despawned.config;

import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ServerStartConfig {
    public static void applyConfig() throws IllegalAccessException {
        int globalDespawnDistance = DespawnedCommonConfig.DEFAULT_DESPAWN_DISTANCE.get();
        int globalNoDespawnDistance = DespawnedCommonConfig.DEFAULT_NO_DESPAWN_DISTANCE.get();
        int worldDespawnDistance = DespawnedWorldConfig.DESPAWN_DISTANCE.get();
        int worldNoDespawnDistance = DespawnedWorldConfig.NO_DESPAWN_DISTANCE.get();
        if (worldDespawnDistance == 0) worldDespawnDistance = globalDespawnDistance;
        if (worldNoDespawnDistance == 0) worldNoDespawnDistance = globalNoDespawnDistance;
        ObfuscationReflectionHelper.findField(MobCategory.class, "despawnDistance").set(MobCategory.CREATURE, worldDespawnDistance);
        ObfuscationReflectionHelper.findField(MobCategory.class, "noDespawnDistance").set(MobCategory.CREATURE, worldNoDespawnDistance);
    }
}
