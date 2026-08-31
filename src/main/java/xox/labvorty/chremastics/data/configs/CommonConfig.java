package xox.labvorty.chremastics.data.configs;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class CommonConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue USE_COIN_ENTITY_WHITELIST = BUILDER
            .comment("If true, only entities listed in coinEntities can drop coins.")
            .define("useCoinEntityWhitelist", true);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> COIN_ENTITIES = BUILDER
            .comment("Entities that drop coins after death")
            .defineListAllowEmpty(
                    "coinEntities",
                    List.of(),
                    () -> "", CommonConfig::validateEntityName
            );

    public static final ModConfigSpec.ConfigValue<List<? extends String>> RESTRICTED_COIN_ENTITIES = BUILDER
            .comment("Entities that will never drop coins after death")
            .defineListAllowEmpty(
                    "restrictedCoinEntities",
                    List.of(),
                    () -> "", CommonConfig::validateEntityName
            );

    public static final ModConfigSpec.BooleanValue REPLACE_EMERALDS_WITH_COINS = BUILDER
            .comment("Whether coins are used instead of emeralds in trades")
            .define(
                    "replaceEmeralds",
                    true
            );

    public static final ModConfigSpec.BooleanValue USE_BAGS_FOR_TRADES = BUILDER
            .comment("Whether villagers use coin bags as trade outputs. Coin bags can store larger and more precise currency values")
            .define(
                    "useCoinBags",
                    false
            );

    public static final ModConfigSpec.IntValue EMERALD_VALUE = BUILDER
            .comment("How much copper coins 1 emerald is worth")
            .defineInRange(
                    "emeraldValue",
                    100,
                    1,
                    10000000
            );

    public static final ModConfigSpec.IntValue COIN_DROP_TYPE = BUILDER
            .comment("Coin drop type, where: 0 - determine value from health, 1 - randomly determine using chance values, 2 - don't drop")
            .defineInRange(
                    "coinDropType",
                    1,
                    0,
                    2
            );

    public static final ModConfigSpec.DoubleValue COPPER_COIN_CHANCE = BUILDER
            .comment("Chance for copper coins to appear in coin bags, and when coinDropType is 1 drop from entities")
            .defineInRange(
                    "copperCoinChance",
                    0.85f,
                    0,
                    1
            );

    public static final ModConfigSpec.IntValue COPPER_COIN_MIN_AMOUNT = BUILDER
            .comment("Minimum number of copper coins in coin bags or random entity drops")
            .defineInRange(
                    "copperCoinMinAmount",
                    1,
                    0,
                    99
            );

    public static final ModConfigSpec.IntValue COPPER_COIN_MAX_AMOUNT = BUILDER
            .comment("Maximum number of copper coins in coin bags or random entity drops")
            .defineInRange(
                    "copperCoinMaxAmount",
                    15,
                    0,
                    99
            );

    public static final ModConfigSpec.DoubleValue SILVER_COIN_CHANCE = BUILDER
            .comment("Chance for silver coins to appear in coin bags, and when coinDropType is 1 drop from entities")
            .defineInRange(
                    "silverCoinChance",
                    0.30f,
                    0,
                    1
            );

    public static final ModConfigSpec.IntValue SILVER_COIN_MIN_AMOUNT = BUILDER
            .comment("Minimum number of silver coins in coin bags or random entity drops")
            .defineInRange(
                    "silverCoinMinAmount",
                    1,
                    0,
                    99
            );

    public static final ModConfigSpec.IntValue SILVER_COIN_MAX_AMOUNT = BUILDER
            .comment("Maximum number of silver coins in coin bags or random entity drops")
            .defineInRange(
                    "silverCoinMaxAmount",
                    4,
                    0,
                    99
            );

    public static final ModConfigSpec.DoubleValue GOLD_COIN_CHANCE = BUILDER
            .comment("Chance for gold coins to appear in coin bags, and when coinDropType is 1 drop from entities")
            .defineInRange(
                    "goldCoinChance",
                    0.01f,
                    0,
                    1
            );

    public static final ModConfigSpec.IntValue GOLD_COIN_MIN_AMOUNT = BUILDER
            .comment("Minimum number of gold coins in coin bags or random entity drops")
            .defineInRange(
                    "goldCoinMinAmount",
                    1,
                    0,
                    99
            );

    public static final ModConfigSpec.IntValue GOLD_COIN_MAX_AMOUNT = BUILDER
            .comment("Maximum number of gold coins in coin bags or random entity drops")
            .defineInRange(
                    "goldCoinMaxAmount",
                    1,
                    0,
                    99
            );

    public static final ModConfigSpec.DoubleValue PLATINUM_COIN_CHANCE = BUILDER
            .comment("Chance for platinum coins to appear in coin bags, and when coinDropType is 1 drop from entities")
            .defineInRange(
                    "platinumCoinChance",
                    0d,
                    0,
                    1
            );

    public static final ModConfigSpec.IntValue PLATINUM_COIN_MIN_AMOUNT = BUILDER
            .comment("Minimum number of platinum coins in coin bags or random entity drops")
            .defineInRange(
                    "platinumCoinMinAmount",
                    0,
                    0,
                    99
            );

    public static final ModConfigSpec.IntValue PLATINUM_COIN_MAX_AMOUNT = BUILDER
            .comment("Maximum number of platinum coins in coin bags or random entity drops")
            .defineInRange(
                    "platinumCoinMaxAmount",
                    0,
                    0,
                    99
            );

    public static final ModConfigSpec.DoubleValue BENT_COIN_CHANCE = BUILDER
            .comment("Chance that bent coin successfully triggers")
            .defineInRange(
                    "bentCoinChance",
                    0.4,
                    0,
                    1
            );

    public static final ModConfigSpec.DoubleValue TRADERS_INSIGNIA_DISCOUNT = BUILDER
            .comment("How much less coins player has to pay for an item when Traders Insignia is equipped")
            .defineInRange(
                    "tradersInsigniaDiscount",
                    0.1,
                    0,
                    1
            );

    public static final ModConfigSpec.BooleanValue DROP_COINS_ON_DEATH = BUILDER
            .comment("Whether player loses coins after death")
            .define(
                    "dropCoins",
                    true
            );

    public static final ModConfigSpec.DoubleValue COINS_LOST_ON_DEATH = BUILDER
            .comment("Percentage of coins lost on death")
            .defineInRange(
                    "coinsLostOnDeath",
                    0.5,
                    0,
                    1
            );

    public static final ModConfigSpec.DoubleValue COIN_BAG_SPAWN_CHANCE = BUILDER
            .comment("How frequently coin bag spawns in chests")
            .defineInRange(
                    "coinBagSpawnChance",
                    0.05,
                    0,
                    1
            );

    public static final ModConfigSpec.DoubleValue ARTIFACTS_SPAWN_CHANCE = BUILDER
            .comment("How frequently artifacts spawn in chests")
            .defineInRange(
                    "artifactsSpawnChance",
                    0.05,
                    0,
                    1
            );

    public static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateEntityName(final Object obj) {
        if (!(obj instanceof String entityName)) return false;

        if (entityName.isBlank()) return false;

        ResourceLocation rl;
        try {
            rl = ResourceLocation.parse(entityName);
        } catch (Exception e) {
            return false;
        }

        return BuiltInRegistries.ENTITY_TYPE.containsKey(rl);
    }
}