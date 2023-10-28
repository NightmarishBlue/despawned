package blue.nightmarish.despawned.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DespawnedCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> DEFAULT_CREATURE_SPAWN_CAP;

    static {
        BUILDER.push("common/default/global configuration for overspawn");
        DEFAULT_CREATURE_SPAWN_CAP = BUILDER
                .comment("the default maximum spawn cap. vanilla is 10 (i think)")
                .comment("this value will be used for all worlds that haven't had a cap configured on them")
                .defineInRange("Default Creature Spawn Cap", 10, 0, 1000); // dunno how to make it greater than 0 without a max.
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
    
}
