package xox.labvorty.chremastics.data.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SPECIAL_EFFECTS = BUILDER
            .comment("Whether particles and other visual effects appear in game")
            .define(
                    "specialEffects",
                    true
            );

    public static final ModConfigSpec.DoubleValue PARTICLE_CHANCE = BUILDER
            .comment("Chance for a particle to appear each frame for coin item entities and coin piles")
            .defineInRange(
                    "particleChance",
                    0.02,
                    0,
                    1
            );

    public static final ModConfigSpec.IntValue PURSE_OVERLAY_X_OFFSET = BUILDER
            .comment("How much X offset purse has")
            .defineInRange(
                    "purseXOffset",
                    0,
                    -1000,
                    1000
            );

    public static final ModConfigSpec.IntValue PURSE_OVERLAY_Y_OFFSET = BUILDER
            .comment("How much Y offset purse has")
            .defineInRange(
                    "purseYOffset",
                    0,
                    -1000,
                    1000
            );

    public static final ModConfigSpec.IntValue PURSE_BUTTON_OVERLAY_X_OFFSET = BUILDER
            .comment("How much X offset purse button has")
            .defineInRange(
                    "purseButtonXOffset",
                    0,
                    -1000,
                    1000
            );

    public static final ModConfigSpec.IntValue PURSE_BUTTON_OVERLAY_Y_OFFSET = BUILDER
            .comment("How much Y offset purse button has")
            .defineInRange(
                    "purseButtonYOffset",
                    0,
                    -1000,
                    1000
            );

    public static final ModConfigSpec SPEC = BUILDER.build();
}
