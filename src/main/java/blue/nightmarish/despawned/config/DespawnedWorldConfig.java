package blue.nightmarish.despawned.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DespawnedWorldConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> CREATURE_SPAWN_CAP;

    static {
        BUILDER.push("per-world configuration for overspawn");
        CREATURE_SPAWN_CAP = BUILDER
                .comment("the maximum spawn cap")
                .comment("if left at 0, the global default will be used")
                .defineInRange("World Creature Spawn Cap", 0, 0, 1000); // dunno how to make it greater than 0 without a max.
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
    
}
