package blue.nightmarish.despawned.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DespawnedWorldConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> DESPAWN_DISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> NO_DESPAWN_DISTANCE;

    static {
        BUILDER.push("global configuration for despawned");
        BUILDER.push("these values will be used for this world");
        DESPAWN_DISTANCE = BUILDER
                .comment("the despawn distance for creatures")
                .comment("mobs further than this distance can despawn")
                .defineInRange("Default Creature Despawn Distance", 0, 0, 1000); // dunno how to make it greater than 0 without a max.
        NO_DESPAWN_DISTANCE = BUILDER
                .comment("the 'no despawn' distance for creatures")
                .comment("mobs closer than this distance to a player will not despawn")
                .defineInRange("Default Creature 'No Despawn' Distance", 0, 0,1000);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
