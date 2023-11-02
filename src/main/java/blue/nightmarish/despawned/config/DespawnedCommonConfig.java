package blue.nightmarish.despawned.config;

import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.common.ForgeConfigSpec;

public class DespawnedCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> DEFAULT_DESPAWN_DISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> DEFAULT_NO_DESPAWN_DISTANCE;

    static {
        int vanillaDespawnDistance = MobCategory.CREATURE.getDespawnDistance();
        int vanillaNoDespawnDistance = MobCategory.CREATURE.getNoDespawnDistance();
        BUILDER.push("global configuration for despawned");
        BUILDER.push("these values will be used for all worlds that haven't had a cap configured on them");
        DEFAULT_DESPAWN_DISTANCE = BUILDER
            .comment("the default despawn distance for creatures")
            .comment("mobs further than this distance can despawn")
            .defineInRange("Default Creature Despawn Distance", vanillaDespawnDistance, 0, 1000); // dunno how to make it greater than 0 without a max.
        DEFAULT_NO_DESPAWN_DISTANCE = BUILDER
            .comment("the default 'no despawn' distance for creatures")
            .comment("mobs closer than this distance to a player will not despawn")
            .defineInRange("Default Creature 'No Despawn' Distance", vanillaNoDespawnDistance, 0,1000);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}